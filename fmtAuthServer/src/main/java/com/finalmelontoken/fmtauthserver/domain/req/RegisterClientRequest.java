package com.finalmelontoken.fmtauthserver.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterClientRequest {
    private String physicalSecretKey;
    private String clientKey;
    private String secretKey;
}
