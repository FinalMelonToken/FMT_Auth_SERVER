package com.finalmelontoken.fmtauthserver.controller;


import com.finalmelontoken.fmtauthserver.domain.req.*;
import com.finalmelontoken.fmtauthserver.domain.res.LoginResponse;
import com.finalmelontoken.fmtauthserver.domain.res.ResponseDto;
import com.finalmelontoken.fmtauthserver.domain.res.SendMailResponse;
import com.finalmelontoken.fmtauthserver.domain.res.TokenResponse;
import com.finalmelontoken.fmtauthserver.exception.ExceptionResponseDto;
import com.finalmelontoken.fmtauthserver.exception.GlobalException;
import com.finalmelontoken.fmtauthserver.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "이메일 보내기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메일 전송 성공", content = @Content(schema = @Schema(implementation = SendMailResponse.class))),
            @ApiResponse(responseCode = "400", description = "구글 메일 인증이 안 된, 학교 메일이 아닌, 이미 가입된 사용자인, 일일 전송 횟수를 초과된, 이미 메일을 보낸 경우", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
    })
    @PostMapping("send-mail")
    public ResponseEntity<?> sendMail(@RequestBody SendMailRequest sendMailRequest) {
        return ResponseDto.ok(authService.sendMail(sendMailRequest));
    }

    @Operation(summary = "학생 계정 인증 후 진짜 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 코드인, 만료올 인증 코드인, 이미 가입된 사용자인 경우", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "구글 메일 인증이 안 된, 메일 전송조차 하지 않은 경우", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
    })
    @PostMapping("join")
    public ResponseEntity<?> join(@RequestBody JoinRequest joinRequest) {
        return ResponseDto.ok(authService.register(joinRequest));
    }
    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refresh token 발급해줌", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "올바르지 않은 이메일/비번", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
    })
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseDto.ok(authService.login(loginRequest));
    }

    @Operation(summary = "AccessToken발급 - 리퀘의 clientKey에 맞게 accessToken SecretKey 써서 발급해줌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 성공", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "리프레시 토큰 에러", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
    })
    @PostMapping("token")
    public ResponseEntity<?> token(
            @RequestHeader("Authorization") String token,
            @RequestBody RefreshRequest refreshRequest
            ) {
        return ResponseDto.ok(authService.refresh(token.substring(7), refreshRequest));
    }

    @Operation(summary = "클라이언트의 Access Token `ClientKey to SecretKey` 딕셔너리 형식으로 등록 **client딴에서는 사용하지 않는 api**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "ClientKey가 이미 있는, 시크릿 키 50자 미만, ClientKey 20자 미만인 경우", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "물리적 키가 틀린 경우", content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
    })
    @PostMapping("register-client")
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientRequest request) {
        return ResponseDto.ok(authService.registerClient(request));
    }
}
