package com.finalmelontoken.fmtauthserver.controller;


import com.finalmelontoken.fmtauthserver.domain.req.*;
import com.finalmelontoken.fmtauthserver.domain.res.ResponseDto;
import com.finalmelontoken.fmtauthserver.service.AuthService;
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
    private final AuthService authService;
    private final GoogleOAuthProvider googleOAuthProvider;

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

    @PostMapping("/send-mail")
    public ResponseEntity<?> sendMail(@RequestBody SendMailRequest sendMailRequest) {
        return ResponseDto.ok(authService.sendMail(sendMailRequest));
    }

    @PostMapping("join")
    public ResponseEntity<?> join(@RequestBody JoinRequest joinRequest) {
        return ResponseDto.ok(authService.register(joinRequest));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseDto.ok(authService.login(loginRequest));
    }

    @PostMapping("token")
    public ResponseEntity<?> token(
            @RequestHeader("Authorization") String token,
            @RequestBody RefreshRequest refreshRequest
            ) {
        return ResponseDto.ok(authService.refresh(token.substring(7), refreshRequest));
    }

    @PostMapping("register-client")
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientRequest request) {
        return ResponseDto.ok(authService.registerClient(request));
    }
}
