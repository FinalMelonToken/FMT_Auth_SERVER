package com.finalmelontoken.fmtauthserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 최상위 예외처리 클래스
 */
@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private String message;
}