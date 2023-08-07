package com.finalmelontoken.fmtauthserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalmelontoken.fmtauthserver.domain.AuthKey;
import com.finalmelontoken.fmtauthserver.domain.GoogleOAuthTokenDto;
import com.finalmelontoken.fmtauthserver.domain.GoogleUserInfoDto;
import com.finalmelontoken.fmtauthserver.domain.Mail;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import com.finalmelontoken.fmtauthserver.util.GoogleOAuthProvider;
import com.finalmelontoken.fmtauthserver.util.MailUtil;
import com.finalmelontoken.fmtauthserver.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserService userService;
    private final MailService mailService;
    private final AuthKeyService authKeyService;
    private final RandomStringUtil randomStringUtil;
    private final GoogleOAuthProvider googleOAuth;

    private final MailUtil mailUtil;

    public String registerSchoolMail(String code) throws IOException {
        GoogleUserInfoDto userInfo = getGoogleUserInfoDto(code);
        String email = userInfo.getEmail();

        if (!mailUtil.isSchoolEmail(email)) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "학교 이메일로 인증해 주세요");
        } else if (userService.findByEmail(email) != null) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다");
        } else if (!authKeyService.isExistKey(email)) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 메일을 전송했어요!");
        }

        // 메일 인증 키 생성
        String authKey = randomStringUtil.generateRandomString(6);
        AuthKey authKeyDto = AuthKey.builder()
                .authKey(authKey)
                .createdTime(Instant.now())
                .email(userInfo.getEmail())
                .build();

        // 만료된 키 삭제
        Instant keyCreatedTime = authKeyService.findByEmail(email).getCreatedTime();
        if (authKeyService.isExistKey(email) && mailUtil.isExpiredMail(keyCreatedTime)) {
            authKeyService.deleteKey(userInfo.getEmail());
        }

        if (!authKeyService.isExistKey(email)) {
            authKeyService.saveKey(authKeyDto);
        }

        // 메일 전송
        mailService.sendMessage(Mail.builder()
                .id(1L)
                .email(userInfo.getEmail())
                .name("정말 대소고 학생이세요??? - 이메일 인증 코드입니당")
                .message(String.format("%s님 안녕하세요\n 인증 코드: %s", userInfo.getName(), authKey))
                .build());

        return "메일이 전송되었어요!";
    }

    public GoogleUserInfoDto getGoogleUserInfoDto(String code) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(code);
        GoogleOAuthTokenDto oAuthTokenDto = googleOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(oAuthTokenDto);
        GoogleUserInfoDto googleUserInfoDto = googleOAuth.getUserInfo(userInfoResponse);
        return googleUserInfoDto;
    }
}
