package com.ndt.be_stepupsneaker.infrastructure.email.util;

import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMailAutoEntity {
    @Autowired
    private final EmailService emailService;

    public SendMailAutoEntity(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendMailAutoPassWordToCustomer(Customer customer) {
        String[] toEmail = new String[1];
        toEmail[0] = customer.getEmail();
        Email email = new Email();
        email.setToEmail(toEmail);
        email.setSubject("STEP UP SNEAKER send your password ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: blue;'>STEP UP SNEAKER send mail your password</h1></div>");
        email.setBody("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><div style='border: 2px dotted #000; padding: 10px; display: inline-block;'><p style='font-size: 16px; font-family: Arial, sans-serif;'>Your password : <strong>" + customer.getPassword() + "</strong></p></div></div>");
        emailService.sendEmail(email);
    }
}
