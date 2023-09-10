package com.finalmelontoken.fmtauthserver.controller;


import com.finalmelontoken.fmtauthserver.domain.req.*;
import com.finalmelontoken.fmtauthserver.domain.res.ResponseDto;
import com.finalmelontoken.fmtauthserver.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class RestAuthController {
    private final AuthService authService;

    @PostMapping("send-mail")
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
            @RequestBody TokenRequest refreshRequest
            ) {
        return ResponseDto.ok(authService.refresh(token.substring(7), refreshRequest));
    }

    @PostMapping("register-client")
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientRequest request) {
        return ResponseDto.ok(authService.registerClient(request));
    }
}
