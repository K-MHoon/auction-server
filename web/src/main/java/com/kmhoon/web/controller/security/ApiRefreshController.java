package com.kmhoon.web.controller.security;


import com.kmhoon.web.controller.dto.security.response.ApiRefreshControllerResponse;
import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.security.SecurityErrorCode;
import com.kmhoon.web.security.jwt.JwtTokenProvider;
import com.kmhoon.web.security.jwt.TokenInfo;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ApiRefreshController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/user/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiRefreshControllerResponse.Refresh refresh(@RequestHeader("Authorization") String authHeader,
                                                        @RequestParam("refreshToken") String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new AuctionApiException(SecurityErrorCode.EMPTY_REFRESH);
        }

        if(!StringUtils.hasText(authHeader) || authHeader.length() < 7) {
            throw new AuctionApiException(SecurityErrorCode.INVALID_STRING);
        }

        String accessToken = authHeader.substring(7);

        if(!checkExpiredToken(accessToken)) {
            return ApiRefreshControllerResponse.Refresh.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        TokenInfo generatedToken = jwtTokenProvider.generate(authentication);

        return ApiRefreshControllerResponse.Refresh.builder()
                .accessToken(generatedToken.getAccessToken())
                .refreshToken(generatedToken.getRefreshToken())
                .build();
    }

    private boolean checkExpiredToken(String accessToken) {
        try {
            jwtTokenProvider.validate(accessToken);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
