package com.ndt.be_stepupsneaker.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class DeployConfig {

    @Value("${url.reset}")
    private String resetUrl;

    @Value("${url.fe.tracking}")
    private String trackingUrl;

    @Value("${url.fe.return}")
    private String returnTrackingUrl;

    @Value("${url.reset.production}")
    private String productionResetUrl;

    @Value("${url.fe.tracking.production}")
    private String productionTrackingUrl;

    @Value("${url.fe.return.production}")
    private String productionReturnTrackingUrl;

    private final Environment environment;

    @Bean
    public String resetUrl() {
        return getUrl("url.reset", "url.reset.production");
    }

    @Bean
    public String trackingUrl() {
        return getUrl("url.fe.tracking", "url.fe.tracking.production");
    }

    @Bean
    public String returnTrackingUrl() {
        return getUrl("url.fe.return", "url.fe.return.production");
    }

    private String getUrl(String localProperty, String productionProperty) {
        if (isProduction()) {
            return environment.getProperty(productionProperty);
        } else {
            return environment.getProperty(localProperty);
        }
    }

    private boolean isProduction() {
        return environment.getActiveProfiles()[0].equalsIgnoreCase("production");
    }
}
