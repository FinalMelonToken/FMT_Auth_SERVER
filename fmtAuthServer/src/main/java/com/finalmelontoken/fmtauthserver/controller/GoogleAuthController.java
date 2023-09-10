package com.finalmelontoken.fmtauthserver.controller;

import com.finalmelontoken.fmtauthserver.service.AuthService;
import com.finalmelontoken.fmtauthserver.util.GoogleOAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/", ""})
public class GoogleAuthController {

    private final GoogleOAuthProvider googleOAuthProvider;
    private final AuthService authService;

    @GetMapping("/google")
    public void getGoogleAuthUrl(HttpServletResponse response) throws Exception {
        response.sendRedirect(googleOAuthProvider.getOauthRedirectURL());
    }

    @GetMapping ("/login/oauth2/code/google")
    public String registerSchoolMail(@RequestParam(name = "code") String code) throws IOException {
        authService.registerSchoolMail(code);
        return "<script>window.close();</script>";
        // TODO: response.sendRedirect로 react웹서버로 전송
    }
}
