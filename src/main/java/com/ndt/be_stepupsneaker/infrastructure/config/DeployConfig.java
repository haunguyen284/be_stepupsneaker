package com.ndt.be_stepupsneaker.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DeployConfig {

    @Value("${url.fe.base}")
    private String feBaseUrl;

    @Value("${url.fe.base.production}")
    private String productionFeBaseUrl;

    @Value("${is.vps:false}")
    private boolean isRunningOnVps;

    @Bean
    public String feBaseUrl() {
        return isRunningOnVps ? productionFeBaseUrl : feBaseUrl;
    }
}
