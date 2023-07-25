package com.finalmelontoken.fmtauthserver.service;

import com.finalmelontoken.fmtauthserver.domain.AuthKey;
import com.finalmelontoken.fmtauthserver.domain.User;
import com.finalmelontoken.fmtauthserver.domain.req.JoinRequest;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import com.finalmelontoken.fmtauthserver.repository.AuthKeyRepository;
import com.finalmelontoken.fmtauthserver.repository.UserRepository;
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

    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }

    public String register(JoinRequest joinRequest) {

        if (!userRepository.existsByEmail(joinRequest.getEmail())) {
            throw new GlobalException(HttpStatus.NO_CONTENT, "구글 인증부터 해주셈");
        }

        if (!authKeyRepository.existsAuthKeyByEmail(joinRequest.getEmail())) {
            throw new GlobalException(HttpStatus.NO_CONTENT, "구글 인증부터 해주셈");
        }
        AuthKey authKey = authKeyRepository.findByEmail(joinRequest.getEmail());
        if (!authKey.getAuthKey().equals(joinRequest.getAuthKey()) || mailUtil.isExpiredMail(authKey.getCreatedTime())) {
            throw new GlobalException(HttpStatus.NO_CONTENT, "잘못된 인증 코드입니다ㅜㅜ");
        }

        userRepository.updateByPassword(joinRequest.getPassword());
        return "회원가입 완료";
    }
}