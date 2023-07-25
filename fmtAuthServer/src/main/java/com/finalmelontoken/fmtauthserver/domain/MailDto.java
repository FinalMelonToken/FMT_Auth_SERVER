package com.finalmelontoken.fmtauthserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDto {
    private Long id;
    private String name;
    private String email;
    private String message;
}