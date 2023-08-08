package com.finalmelontoken.fmtauthserver.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String redirectUrl;
}
