package com.ndt.be_stepupsneaker.infrastructure.email.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String [] toEmail;
    private String subject;
    private String body;
    private String titleEmail;
}
