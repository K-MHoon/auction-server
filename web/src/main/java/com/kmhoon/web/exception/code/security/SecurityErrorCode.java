package com.kmhoon.web.exception.code.security;

import com.kmhoon.web.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {

    EMPTY_REFRESH("S001", HttpStatus.BAD_REQUEST, "리프레시 토큰이 존재하지 않습니다."),
    INVALID_STRING("S002", HttpStatus.BAD_REQUEST, "잘못된 Authorization Header 입니다."),
    VALIDATION_TOKEN("S003", HttpStatus.UNAUTHORIZED, "토큰 인증에 실패했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
