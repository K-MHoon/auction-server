package com.kmhoon.web.config.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.kmhoon.common.repository")
@EntityScan(basePackages = "com.kmhoon.common.model.entity")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return Optional::empty;
        }
        return () -> Optional.of(authentication.getName());
    }
}
