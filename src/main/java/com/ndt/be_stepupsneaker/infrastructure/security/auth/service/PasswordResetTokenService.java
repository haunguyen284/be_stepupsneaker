package com.ndt.be_stepupsneaker.infrastructure.security.auth.service;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.entity.auth.PasswordResetToken;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.PasswordResetRequest;
import com.ndt.be_stepupsneaker.repository.auth.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ClientCustomerRepository clientCustomerRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private boolean isEmailSending = false;
    private long timeRemaining = 0;

    public boolean sendPasswordResetEmail(String email) {
        if (isEmailSending) {
            throw new ApiException("Please try again later. Time remaining: " + timeRemaining + "s");
        }
        Customer customer = clientCustomerRepository.findByEmail(email).orElse(null);
        Employee employee = adminEmployeeRepository.findByEmail(email).orElse(null);
        if (customer == null && employee == null) {
            throw new ApiException("User" + EntityProperties.NOT_FOUND);
        }
        String token = UUID.randomUUID().toString();
        if (sendMailUrl(customer, employee, token)) {
            isEmailSending = true;
            timeRemaining = 59;
            scheduleEmailReset();
            return true;
        }
        return false;
    }

    public boolean resetPassword(String token, PasswordResetRequest resetRequest) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken != null && !resetToken.getExpirationTime().before(new Date())) {
            Customer customer = resetToken.getCustomer();
            Employee employee = resetToken.getEmployee();
            if (!resetRequest.getConfirmPassword().equals(resetRequest.getNewPassword())) {
                throw new ApiException("Re-entered password is incorrect!");
            }
            if (customer != null) {
                customer.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
                clientCustomerRepository.save(customer);
                passwordResetTokenRepository.delete(resetToken);
                return true;
            }
            employee.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
            adminEmployeeRepository.save(employee);
            passwordResetTokenRepository.delete(resetToken);
            return true;
        }
        return false;
    }

    public boolean sendMailUrl(Customer customer, Employee employee, String token) {
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        PasswordResetToken resetToken = createPasswordResetToken(customer, employee, token);
        String resetLink = EntityProperties.URL_RESET + resetToken.getToken();
        if (customer != null) {
            sendMailAutoEntity.sendMailAutoResetPassword(customer.getEmail(), resetLink);
            return true;
        }
        sendMailAutoEntity.sendMailAutoResetPassword(employee.getEmail(), resetLink);
        return true;
    }

    public PasswordResetToken createPasswordResetToken(Customer customer, Employee employee,
                                                       String passwordToken) {
        PasswordResetToken existingToken = passwordResetTokenRepository.findByCustomerAndEmployee(customer, employee);

        if (existingToken != null && !existingToken.getExpirationTime().before(new Date())) {
            existingToken.setToken(passwordToken);
            existingToken.setExpirationTime(calculateExpirationDate());
            return passwordResetTokenRepository.save(existingToken);
        } else {
            if (existingToken != null) {
                passwordResetTokenRepository.delete(existingToken);
            }
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(passwordToken);
            passwordResetToken.setExpirationTime(calculateExpirationDate());
            passwordResetToken.setCustomer(customer);
            passwordResetToken.setEmployee(employee);
            return passwordResetTokenRepository.save(passwordResetToken);
        }
    }

    private void scheduleEmailReset() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (timeRemaining > 0) {
                timeRemaining--;
            }
            if (timeRemaining == 0) {
                isEmailSending = false;
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, EntityProperties.EXPIRATION);
        return calendar.getTime();
    }
}
