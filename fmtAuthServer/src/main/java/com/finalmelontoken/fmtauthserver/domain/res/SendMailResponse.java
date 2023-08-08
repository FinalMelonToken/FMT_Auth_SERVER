package com.finalmelontoken.fmtauthserver.domain.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMailResponse {

    @Schema(description = "이메일 팝업 타이틀")
    private String message;

    @Schema(description = "이메일 팝업 서브 타이틀")
    private String detail;
}
