package com.finalmelontoken.fmtauthserver;

import com.finalmelontoken.fmtauthserver.domain.TokenResponse;
import com.finalmelontoken.fmtauthserver.domain.req.JoinRequest;
import com.finalmelontoken.fmtauthserver.domain.req.LoginRequest;
import com.finalmelontoken.fmtauthserver.service.UserService;
import com.finalmelontoken.fmtauthserver.util.GoogleOAuthProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class FmtAuthServerApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void register() {
        userService.register(new JoinRequest(
                "hhhello0507@dgsw.hs.kr",
                "S6FV8q",
                encoder.encode("pqweqwqwe")
        ));
    }

//    @Test
    public void refresh() {
        //        accessToken=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaGhlbGxvMDUwN0BkZ3N3LmhzLmtyIiwiZXhwIjoxNjkwMzg1NDE2fQ.U6OonNYrjg2Qm8mq--qCIhfTz8Q-110gsN6IrwXQyYk
//        refreshToken=Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaGhlbGxvMDUwN0BkZ3N3LmhzLmtyIiwiZXhwIjoxNjkwOTg2NjE2fQ.RRxvaOkHhkjILky1j65W5OgmaYDkgVEYp70vo7Ww4XQ

        TokenResponse tokenInfo = userService.refresh("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaGhlbGxvMDUwN0BkZ3N3LmhzLmtyIiwiZXhwIjoxNjkwOTg2NjE2fQ.RRxvaOkHhkjILky1j65W5OgmaYDkgVEYp70vo7Ww4XQ");
        System.out.println(tokenInfo);
    }

    @Autowired
    GoogleOAuthProvider googleOAuth;
    @Test
    public void a() {
        System.out.println(googleOAuth.getOauthRedirectURL());
    }

}
