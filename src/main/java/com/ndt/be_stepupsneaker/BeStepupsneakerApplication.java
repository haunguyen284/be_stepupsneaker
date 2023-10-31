package com.ndt.be_stepupsneaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BeStepupsneakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeStepupsneakerApplication.class, args);
    }

}
