package com.ndt.be_stepupsneaker.infrastructure.email.util;

import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendMailAutoEntity {
    @Autowired
    private final EmailService emailService;

    public SendMailAutoEntity(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendMailAutoPassWord(Customer customer, String passWord, Employee employee) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject("STEP UP SNEAKER sends you the password ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>STEP UP SNEAKER sends you the password</h1></div>");
        email.setBody("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><div style='border: 2px dotted #000; padding: 10px; display: inline-block;'><p style='font-size: 16px; font-family: Arial, sans-serif;'>Your password : <strong>" + passWord + "</strong></p></div></div>");
        if (customer != null) {
            toEmail[0] = customer.getEmail();
        } else {
            toEmail[0] = employee.getEmail();
        }
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoVoucherToCustomer(List<CustomerVoucher> customerVouchers) {
        Email email = new Email();
        email.setSubject("STEP UP SNEAKER sends you the new Voucher ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>STEP UP SNEAKER sends you the new Voucher </h1></div>");
        Voucher voucher = customerVouchers.get(0).getVoucher();
        String voucherInfo = "<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'>";
        voucherInfo += "<div style='border: 2px dotted #000; padding: 10px; display: inline-block;'>";
        voucherInfo += "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Code: <strong>" + voucher.getCode() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Name: <strong>" + voucher.getName() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Status: <strong>" + voucher.getStatus() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Type: <strong>" + voucher.getType() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Value: <strong>" + voucher.getValue() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Constraint: <strong>" + voucher.getConstraint() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>Start Date: <strong>" + voucher.getStartDate() + "</strong></p>"
                + "<p style='font-size: 16px; font-family: Arial, sans-serif;'>End Date: <strong>" + voucher.getEndDate() + "</strong></p>";
        voucherInfo += "</div></div>";
        email.setBody(voucherInfo);
        String[] toEmail = customerVouchers
                .stream()
                .map(customerVoucher -> customerVoucher.getCustomer().getEmail())
                .toArray(String[]::new);
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }
}
