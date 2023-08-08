package com.finalmelontoken.fmtauthserver.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidTokenRequest {
    private String token;
    private String clientKey;
}