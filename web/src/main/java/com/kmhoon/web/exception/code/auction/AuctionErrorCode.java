package com.kmhoon.web.exception.code.auction;

import com.kmhoon.web.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

    NOT_FINISHED_AUCTION_EXIST("S-AUCTION-001",HttpStatus.BAD_REQUEST, "해당 물품은 이미 진행중인 경매가 존재합니다."),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
