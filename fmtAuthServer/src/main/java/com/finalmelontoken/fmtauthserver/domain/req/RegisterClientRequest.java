package com.finalmelontoken.fmtauthserver.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Client 정보 등록 리퀘")
public class RegisterClientRequest {
    @Schema(description = "물리적 키")
    private String physicalSecretKey;

    private String clientKey;

    private String secretKey;
}
