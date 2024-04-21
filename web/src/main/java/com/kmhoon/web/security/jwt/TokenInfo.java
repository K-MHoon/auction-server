package com.kmhoon.web.security.jwt;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TokenInfo {

    private String grantType;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;
}
