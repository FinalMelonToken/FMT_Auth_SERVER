package com.finalmelontoken.fmtauthserver.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMailResponse {
    private String message;
    private String detail;
}
