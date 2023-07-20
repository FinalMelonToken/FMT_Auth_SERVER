package com.finalmelontoken.fmtauthserver.controller;

import com.finalmelontoken.fmtauthserver.domain.User;
import com.finalmelontoken.fmtauthserver.domain.res.ResponseDto;
import com.finalmelontoken.fmtauthserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/sign")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/up")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        return new ResponseEntity<>(
                ResponseDto.builder()
                        .status(200)
                        .data(userService.register(user))
                        .build()
                , HttpStatus.OK
        );
    }

    @PostMapping("/in")
    public ResponseEntity<?> signIn(@RequestBody User user) {
        return new ResponseEntity<>(
                ResponseDto.builder()
                        .status(200)
                        .data(userService.login(user))
                        .build()
                , HttpStatus.OK
        );
    }


}
