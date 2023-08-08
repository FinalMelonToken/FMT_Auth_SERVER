package com.finalmelontoken.fmtauthserver.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinRequest {
    private String email;

    private String password;

    @Schema(description = "이메일 인증 키")
    private String authKey;
}