package com.matchhub.matchhub.security.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
@RequiredArgsConstructor
public class AuditConfig {
    @Bean
    public AuditorAware<Long> auditorAware() {
        return new HubUserAuditAware();
    }
}
