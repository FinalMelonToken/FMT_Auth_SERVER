package com.finalmelontoken.fmtauthserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleUserInfoDto {

    private String id;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String picture;
    private String locale;
}
