package com.kmhoon.web.exception.code.auction;

import com.kmhoon.web.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

    NOT_FINISHED_AUCTION_EXIST("S-AUCTION-001",HttpStatus.BAD_REQUEST, "해당 물품은 이미 진행 또는 진행 대기중인 경매가 존재합니다."),
    START_DATE_NOT_GE_END_DATE("S-AUCTION-002",HttpStatus.BAD_REQUEST, "시작일자는 종료일자보다 크거나 같을 수 없습니다."),
    BETWEEN_ONE_WEEK("S-AUCTION-003",HttpStatus.BAD_REQUEST, "시작일자는 종료일자는 최대 1주일까지 가능합니다."),
    HAS_NOT_COUPON("S-AUCTION-004",HttpStatus.BAD_REQUEST, "사용가능한 쿠폰이 존재하지 않습니다."),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
