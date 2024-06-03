package com.kmhoon.web.exception.handler;

import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuctionApiException.class)
    public ResponseEntity<ErrorResponse> handleAuctionApiException(AuctionApiException ex) {
        log.debug("cause Auction Api Exception = ", ex);
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ErrorResponse.of(ex.getErrorCode()));
    }
}
