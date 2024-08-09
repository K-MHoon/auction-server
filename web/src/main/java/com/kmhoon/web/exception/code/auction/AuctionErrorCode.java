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
    SEQ_NOT_FOUND_OR_NOT_RUNNING("S-AUCTION-005",HttpStatus.BAD_REQUEST, "해당 경매가 존재하지 않거나, 진행중인 상태가 아닙니다."),
    OVER_MAX_PARTICIPANT_COUNT("S-AUCTION-006",HttpStatus.BAD_REQUEST, "해당 경매의 최대 참가자 수를 초과했습니다."),
    AUCTION_NOT_FOUND("S-AUCTION-007",HttpStatus.BAD_REQUEST, "해당 경매 정보를 찾을 수 없습니다."),
    PRICE_LESS_THAN_BEFORE_PRICE("S-AUCTION-008",HttpStatus.BAD_REQUEST, "요청한 가격이 현재 경매 가격과 같거나 낮습니다."),
    PRICE_LESS_THAN_HAS_MONEY("S-AUCTION-009",HttpStatus.BAD_REQUEST, "현재 보유한 금액보다 더 높은 금액을 요청했습니다."),
    PRICE_LESS_THAN_NEXT_PRICE("S-AUCTION-010",HttpStatus.BAD_REQUEST, "최소 다음 금액 미만의 금액을 요청했습니다."),
    NOT_MATCH_SUBSCRIBE_USER("S-AUCTION-011",HttpStatus.BAD_REQUEST, "구독 요청한 사용자와 로그인한 사용자가 일치하지 않습니다."),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
