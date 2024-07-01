package com.kmhoon.web.socket.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuctionParticipantDto implements RedisMessage {

    private String type;
    private AuctionParticipantMessage data;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static final class AuctionParticipantMessage {

        private Long auctionSeq;
        private Long userSeq;
        private Long count;
    }

    public static AuctionParticipantDto create(AuctionParticipantMessage message) {
        return new AuctionParticipantDto("PARTICIPANT", message);
    }

}
