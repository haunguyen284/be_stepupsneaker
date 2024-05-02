package com.ndt.be_stepupsneaker.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class DeployConfig {
    @Value("${port.fe.deploy}")
    private String portDeployFe;
}