package com.ndt.be_stepupsneaker.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = "com.ndt.be_stepupsneaker.infrastructure.config")
@Getter
@Setter
public class DeployConfig {

    @Value("${url.reset}")
    private String resetUrl;

    @Value("${url.fe.tracking}")
    private String feTrackingUrl;

    @Value("${url.fe.return}")
    private String feReturnUrl;
}
