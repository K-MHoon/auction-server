package com.kmhoon.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuctionStatus {

    BEFORE("B"), // 경매 진행 전
    STOPPED("S"), // 경매 중단
    RUNNING("R"), // 경매 중
    FINISHED("F"); // 경매 완료

    private final String code;
}
