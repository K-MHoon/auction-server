package com.kmhoon.web.exception.code.common;

import com.kmhoon.web.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER("C001", HttpStatus.BAD_REQUEST, "잘못된 파라미터가 포함되어있습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
