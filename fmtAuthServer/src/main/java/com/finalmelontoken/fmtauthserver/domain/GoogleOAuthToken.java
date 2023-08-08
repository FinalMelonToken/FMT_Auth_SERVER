package com.finalmelontoken.fmtauthserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleOAuthToken {
    private String access_token;
    private Integer expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
