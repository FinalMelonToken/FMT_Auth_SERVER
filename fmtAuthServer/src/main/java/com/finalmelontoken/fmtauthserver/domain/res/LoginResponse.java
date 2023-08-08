package com.finalmelontoken.fmtauthserver.domain.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    @Schema(description = "refresh token")
    private String token;

    @Schema(description = "리다이렉션 URL")
    private String redirectUrl;
}
