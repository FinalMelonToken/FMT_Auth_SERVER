package com.finalmelontoken.fmtauthserver.domain.res;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ResponseDto<T> {
    private int status;
    private T data;

    public ResponseDto(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseEntity<ResponseDto<T>> build() {
        return ResponseEntity.status(HttpStatus.valueOf(status)).body(this);
    }

    public static <T> ResponseEntity<ResponseDto<T>> ok(T body) {
        return new ResponseDto<>(HttpStatus.OK.value(), body).build();
    }

    public static ResponseEntity<ResponseDto<Object>> accepted() {
        return new ResponseDto<>(HttpStatus.ACCEPTED.value(), null).build();
    }

    public static ResponseEntity<ResponseDto<Object>> noContent() {
        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), null).build();
    }

    public static <T> ResponseEntity<ResponseDto<T>> of(HttpStatus status, T body) {
        return new ResponseDto<>(status.value(), body).build();
    }
}
