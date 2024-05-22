package com.kmhoon.web.exception;

import com.kmhoon.web.exception.code.AbstractCommonModuleErrorCode;
import com.kmhoon.web.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class AuctionApiException extends RuntimeException{

    private final ErrorCode errorCode;

    public AuctionApiException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public AuctionApiException(Throwable cause, String message) {
        super(cause);
        this.errorCode = new AbstractCommonModuleErrorCode() {
            @Override
            public String getMessage() {
                return message;
            }
        };
    }
}
