package com.finalmelontoken.fmtauthserver.service;

import com.finalmelontoken.fmtauthserver.domain.AuthKey;
import com.finalmelontoken.fmtauthserver.domain.TokenInfo;
import com.finalmelontoken.fmtauthserver.domain.User;
import com.finalmelontoken.fmtauthserver.domain.req.JoinRequest;
import com.finalmelontoken.fmtauthserver.domain.req.LoginRequest;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import com.finalmelontoken.fmtauthserver.repository.AuthKeyRepository;
import com.finalmelontoken.fmtauthserver.repository.UserRepository;
import com.finalmelontoken.fmtauthserver.util.JwtTokenProvider;
import com.finalmelontoken.fmtauthserver.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthKeyRepository authKeyRepository;
    private final MailUtil mailUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }

    public String register(JoinRequest joinRequest) {

        if (!userRepository.existsByEmail(joinRequest.getEmail()) || !authKeyRepository.existsAuthKeyByEmail(joinRequest.getEmail())) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "학생 구글 계정 인증 먼저 해주세요");
        }

        if (userRepository.findByEmail(joinRequest.getEmail()).getPassword() != null) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "이미 가입된 사용자입니다");
        }

        AuthKey authKey = authKeyRepository.findByEmail(joinRequest.getEmail());
        if (!authKey.getAuthKey().equals(joinRequest.getAuthKey()) || mailUtil.isExpiredMail(authKey.getCreatedTime())) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "잘못된 인증 코드입니다ㅜㅜ");
        }

        authKeyRepository.deleteByEmail(joinRequest.getEmail());

        userRepository.updateByPassword(joinRequest.getPassword());
        return "회원가입 완료";
    }

    public TokenInfo login(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();

        if (!userRepository.existsByEmail(email)) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "올바른 이메일을 입력해주세요");
        }

        if (!userRepository.findByEmail(email).getPassword().equals(loginRequest.getPassword())) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "올바른 비밀번호를 입력해주세요");
        }

        return jwtTokenProvider.generateToken(loginRequest.getEmail());
    }

    public TokenInfo refresh(String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            String email = jwtTokenProvider.getRefreshSubFromToken(refreshToken);
            String accessToken = jwtTokenProvider.generateToken(email).getAccessToken();
            return new TokenInfo(
                    accessToken,
                    refreshToken
            );
        }

        throw new GlobalException(HttpStatus.UNAUTHORIZED, "리프레시 토큰 만료");
    }
}