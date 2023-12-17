package com.kmhoon.config.security.jwt;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TokenInfo {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
