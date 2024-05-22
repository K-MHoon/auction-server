package com.kmhoon.web.exception.code.service.item;

import com.kmhoon.web.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemErrorCode implements ErrorCode {

    OVER_ITEM_LIMIT("S-ITEM-001",HttpStatus.BAD_REQUEST, "아이템 최대 보유 수량을 초과했습니다."),
    HAS_NOT_SEQ_REQUEST("S-ITEM-002",HttpStatus.BAD_REQUEST, "존재하지 않는 Item을 포함하고 있습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
