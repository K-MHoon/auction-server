package com.kmhoon.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("within(com.kmhoon.web.controller..*)") // 패키지 범위 설정
    public void controller() {}

    @Before("controller()")
    public void beforeControllerLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(method.getName()).append("] ");
        sb.append("called By ").append(authentication == null ? "None" : authentication.getName()).append(" ");
        sb.append("[");

        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            sb.append(parameters[i].getName()).append(" = ").append(args[i]);
            if(i != (args.length - 1)) {
                sb.append(", ");
            }
        }
        sb.append("]");
        log.info(sb.toString());
    }
}
