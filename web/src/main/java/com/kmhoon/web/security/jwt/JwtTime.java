package com.kmhoon.web.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtTime {

    @Value("${jwt.time.access}")
    private Long accessTokenTime;

    @Value("${jwt.time.refresh}")
    private Long refreshTokenTime;
}
