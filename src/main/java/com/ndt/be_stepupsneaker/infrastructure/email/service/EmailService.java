package com.ndt.be_stepupsneaker.infrastructure.email.service;

import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;

public interface EmailService {

    void sendEmail(Email details);

}
