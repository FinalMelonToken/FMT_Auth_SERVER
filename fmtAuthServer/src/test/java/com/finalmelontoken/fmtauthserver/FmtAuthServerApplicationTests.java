package com.finalmelontoken.fmtauthserver;

import com.finalmelontoken.fmtauthserver.domain.req.JoinRequest;
import com.finalmelontoken.fmtauthserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FmtAuthServerApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void register() {
        userService.register(new JoinRequest(
                "hhhello0507@dgsw.hs.kr",
                "eZT3yw",
                "pqweqwqwe"
        ));
    }
}
