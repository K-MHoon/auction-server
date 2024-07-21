package com.kmhoon.web.security.jwt;

import com.kmhoon.web.exception.AuctionApiException;
import com.kmhoon.web.exception.code.security.SecurityErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final HandlerExceptionResolver resolver;

    public JwtFilter(JwtTokenProvider jwtTokenProvider, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.resolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request, response);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            this.resolver.resolveException(request, response, null, new AuctionApiException(SecurityErrorCode.VALIDATION_TOKEN, ex));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request, HttpServletResponse response) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Bearer 헤더가 존재하지 않습니다.");
        }
        String token = bearerToken.substring(7);
        jwtTokenProvider.validate(token);
        return token;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        if(path.startsWith("/api/user/")) {
            return true;
        }

        if(path.startsWith("/api/view/")) {
            return true;
        }

        return false;
    }
}
