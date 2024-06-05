package com.kmhoon.batch.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.kmhoon.common.repository")
@EntityScan(basePackages = "com.kmhoon.common.model.entity")
public class JpaConfig {

}
