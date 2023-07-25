package com.finalmelontoken.fmtauthserver.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinRequest {
    private String email;
    private String authKey;
    private String password;
}
