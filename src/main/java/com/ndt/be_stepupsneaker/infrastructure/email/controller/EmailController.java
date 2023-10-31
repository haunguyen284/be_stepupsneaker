package com.ndt.be_stepupsneaker.infrastructure.email.controller;

import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = {"*"})
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping()
    public ResponseEntity<?> sendMail(@RequestBody Email email) {
        emailService.sendEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
