package com.ndt.be_stepupsneaker.infrastructure.email.service.impl;

import com.ndt.be_stepupsneaker.infrastructure.constant.MailConstant;
import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    @Async
    public void sendEmail(Email details) {
        String htmlBody = MailConstant.BODY_STARTS +
                details.getTitleEmail() +
//                MailConstant.BODY_BODY +
                details.getBody() +
                MailConstant.BODY_END;
        sendSimpleMail(details.getToEmail(), htmlBody, details.getSubject());
    }

    private void sendSimpleMail(String[] recipients, String msgBody, String subject) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            ClassPathResource resource = new ClassPathResource(MailConstant.LOGO_PATH);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setBcc(recipients);
            mimeMessageHelper.setText(msgBody, true);
            mimeMessageHelper.setSubject(subject);
//            mimeMessageHelper.addInline("logoImage", resource);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("ERROR WHILE SENDING MAIL: {}", e.getMessage());
        }
    }
}
