package com.kmhoon.web.controller.dto.security.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiRefreshControllerResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @ToString
    public static class Refresh {

        private String accessToken;
        private String refreshToken;
    }
}
