package com.finalmelontoken.fmtauthserver.controller;


import com.finalmelontoken.fmtauthserver.domain.req.JoinRequest;
import com.finalmelontoken.fmtauthserver.domain.req.LoginRequest;
import com.finalmelontoken.fmtauthserver.domain.res.ResponseDto;
import com.finalmelontoken.fmtauthserver.service.OAuthService;
import com.finalmelontoken.fmtauthserver.service.UserService;
import com.finalmelontoken.fmtauthserver.util.GoogleOAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RestAuthController {
    private final UserService userService;
    private final OAuthService oauthService;
    private final GoogleOAuthProvider googleOAuthProvider;

    @GetMapping("/google")
    public void getGoogleAuthUrl(HttpServletResponse response) throws Exception {
        response.sendRedirect(googleOAuthProvider.getOauthRedirectURL());
    }

    /*@GetMapping ("/login/oauth2/code/google")
    public ResponseEntity<?> registerSchoolMail(@RequestParam(name = "code") String code) throws IOException {
        return ResponseDto.ok(oauthService.registerSchoolMail(code));
    }*/
    @GetMapping ("/login/oauth2/code/google")
    public String registerSchoolMail(@RequestParam(name = "code") String code) throws IOException {
        oauthService.registerSchoolMail(code);
        return "<script>window.close();</script>";
    }

    @PostMapping("join")
    public ResponseEntity<?> join(@RequestBody JoinRequest joinRequest) {
        return ResponseDto.ok(userService.register(joinRequest));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseDto.ok(userService.login(loginRequest));
    }

    @PostMapping("token")
    public ResponseEntity<?> token(@RequestHeader("Authorization") String token) {
        return ResponseDto.ok(userService.refresh(token.substring(7)));
    }
}
