package com.finalmelontoken.fmtauthserver.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidTokenResponse {
    private String token;
}
