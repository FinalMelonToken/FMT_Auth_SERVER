package com.finalmelontoken.fmtauthserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalmelontoken.fmtauthserver.domain.*;
import com.finalmelontoken.fmtauthserver.domain.req.*;
import com.finalmelontoken.fmtauthserver.domain.res.LoginResponse;
import com.finalmelontoken.fmtauthserver.domain.res.SendMailResponse;
import com.finalmelontoken.fmtauthserver.domain.res.TokenResponse;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import com.finalmelontoken.fmtauthserver.repository.AuthKeyRepository;
import com.finalmelontoken.fmtauthserver.repository.SecretKeyRepository;
import com.finalmelontoken.fmtauthserver.repository.UserRepository;
import com.finalmelontoken.fmtauthserver.util.GoogleOAuthProvider;
import com.finalmelontoken.fmtauthserver.util.JwtTokenProvider;
import com.finalmelontoken.fmtauthserver.util.MailUtil;
import com.finalmelontoken.fmtauthserver.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthKeyRepository authKeyRepository;
    private final SecretKeyRepository secretKeyRepository;
    private final MailUtil mailUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder encoder;
    private final RandomStringUtil randomStringUtil;
    private final GoogleOAuthProvider googleOAuthProvider;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public SendMailResponse sendMail(SendMailRequest sendMailRequest) {
        String email = sendMailRequest.getEmail();
        int count = 0;

        if (!userRepository.existsByEmail(email))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이메일 인증을 먼저 해주세요");
        else if (authKeyRepository.existsAuthKeyByEmail(email)){
            Instant createdTime = authKeyRepository.findByEmail(email).getCreatedTime();
            if (!mailUtil.isToday(createdTime)) {
                authKeyRepository.deleteByEmail(email);
            }
        }
        if (!mailUtil.isSchoolEmail(email))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "학교 이메일로 인증해 주세요");
        if (userRepository.findByEmail(email).getPassword() != null)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다");
        if (authKeyRepository.existsAuthKeyByEmail(email)) {

            AuthKey authKey = authKeyRepository.findByEmail(email);
            count = authKey.getCount();

            if (authKey.getCount() >= 5)
                throw new GlobalException(HttpStatus.BAD_REQUEST, "일일 전송 횟수를 초과했어요");
            if (!mailUtil.isExpiredMail(authKey.getCreatedTime()))
                throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 메일을 전송했어요!");
        }

        // 만료된 키는 삭제
        if (authKeyRepository.existsAuthKeyByEmail(email) && mailUtil.isExpiredMail(authKeyRepository.findByEmail(email).getCreatedTime()))
            authKeyRepository.deleteByEmail(email);

        // 메일 인증 키 생성
        String authKey = randomStringUtil.generateRandomString(6);

        AuthKey authKeyDto = AuthKey.builder()
                .authKey(authKey)
                .createdTime(Instant.now())
                .email(email)
                .count(count + 1)
                .build();

        authKeyRepository.save(authKeyDto);

        mailUtil.sendMessage(Mail.builder()
                .id(1L)
                .email(email)
                .name("정말 대소고 학생이세요??? - 이메일 인증 코드입니당")
                .message(String.format("%s님 안녕하세요\n 인증 코드: %s\n %d회 남았어요", email, authKey, 5 - count))
                .build());

        return new SendMailResponse(
                "삐빅 학생입니다..",
                email + "로 인증 코드를 전송했어요!"
        );
    }

    public void registerSchoolMail(String code) throws IOException {
        GoogleUserInfo userInfo = getGoogleUserInfoDto(code);
        String email = userInfo.getEmail();

        // TODO : throw 터트리는 게 아니라 uri path return
        if (userRepository.existsByEmail(email)) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다");
        } else if (!mailUtil.isSchoolEmail(email)) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "학교 계정이 아닙니다");
        }
        userRepository.save(
                User.builder()
                        .email(userInfo.getEmail())
                        .role(UserRole.ROLE_USER)
                        .point(0L)
                        .build()
        );
    }

    public GoogleUserInfo getGoogleUserInfoDto(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuthProvider.requestAccessToken(code);
        GoogleOAuthToken oAuthTokenDto = googleOAuthProvider.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOAuthProvider.requestUserInfo(oAuthTokenDto);
        GoogleUserInfo googleUserInfoDto = googleOAuthProvider.getUserInfo(userInfoResponse);
        return googleUserInfoDto;
    }

    public String register(JoinRequest joinRequest) {

        String email = joinRequest.getEmail();
        if (!userRepository.existsByEmail(email))
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "학생 구글 계정 인증 먼저 해주세요");
        if (userRepository.findByEmail(email).getPassword() != null)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다");
        if (!authKeyRepository.existsAuthKeyByEmail(email))
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "메일 전송을 해주세요");

        AuthKey authKey = authKeyRepository.findByEmail(email);
        if (!authKey.getAuthKey().equals(joinRequest.getAuthKey()))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다");
        if (mailUtil.isExpiredMail(authKey.getCreatedTime()))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "만료된 인증 코드 입니다");

        authKeyRepository.deleteByEmail(email);
        userRepository.updateByPassword(encoder.encode(joinRequest.getPassword()));
        return "회원가입 완료";
    }

    public LoginResponse login(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();

        if (!userRepository.existsByEmail(email))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "올바른 이메일을 입력해주세요");
        if (!encoder.matches(loginRequest.getPassword(), userRepository.findByEmail(email).getPassword()))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "올바른 비밀번호를 입력해주세요");

        return new LoginResponse(
                jwtTokenProvider.generateRefreshToken(email).getToken(),
                loginRequest.getRedirectUrl()
        );
    }

    public TokenResponse refresh(String refreshToken, RefreshRequest refreshRequest) {
        jwtTokenProvider.validateRefreshToken(refreshToken);

        String email = jwtTokenProvider.getRefreshSubFromToken(refreshToken);
        TokenResponse response = jwtTokenProvider.generateAccessToken(email, refreshRequest);

        return response;
    }

    public String registerClient(RegisterClientRequest request) {
        if (!request.getPhysicalSecretKey().equals("이잉"))
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "물리적 키가 틀렸습니다");
        if (secretKeyRepository.existsByClientKey(request.getClientKey()))
            throw new GlobalException(HttpStatus.BAD_REQUEST, "해당 ClientKey가 이미 있습니다");
        if (request.getClientKey().length() < 20)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "ClientKey를 안전하게 20자 이상으로 해주세요");
        if (request.getSecretKey().length() < 50)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "시크릿 키를 안전하게 50자 이상으로 해주세요");
        String encodedSecretKey = Base64.getEncoder().encodeToString(request.getSecretKey().getBytes());
        secretKeyRepository.save(
                SecretKey.builder()
                        .clientKey(request.getClientKey())
                        .secretKey(encodedSecretKey)
                        .build()
        );
        return "success - " + request.getClientKey();
    }
}