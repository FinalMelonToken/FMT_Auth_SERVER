package com.finalmelontoken.fmtauthserver.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExceptionResponseDto {
    @JsonProperty(value = "timestamp", index = 1)
    private final String timeStamp = LocalDateTime.now().toString();
    private final int status;
    private final HttpStatus error;
    private final String message;
}
