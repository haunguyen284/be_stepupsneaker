package com.ndt.be_stepupsneaker.infrastructure.security.auth.controller;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.AuthenticationResponse;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.AuthenticationRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.ChangePasswordRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.PasswordResetRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.service.AuthenticationService;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.service.PasswordResetTokenService;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final MySessionInfo mySessionInfo;
    private final MessageUtil messageUtil;


    @GetMapping("/admin/me")
    public Object getMe() {
        return ResponseHelper.getResponse(mySessionInfo.getCurrentEmployee(), HttpStatus.OK);
    }


    @GetMapping("/client/me")
    public Object getMeCustomer() {
        return ResponseHelper.getResponse(mySessionInfo.getCurrentCustomer(), HttpStatus.OK);
    }


    @PostMapping("/register-employees")
    public ResponseEntity<AuthenticationResponse> registerEmployee(
            @RequestBody AdminEmployeeRequest request) {
        return ResponseEntity.ok(authenticationService.registerEmployee(request));

    }

    @PostMapping("/register-customers")
    public ResponseEntity<AuthenticationResponse> registerCustomer(
            @RequestBody ClientCustomerRequest request) {
        return ResponseEntity.ok(authenticationService.registerCustomer(request));

    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticate = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticate);

    }

    @PutMapping("/change-password")
    public Object changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return ResponseHelper.getResponse(authenticationService.changePassword(request), HttpStatus.OK);
    }


    @PostMapping("/forgot-password")
    public Object forgotPassword(@RequestParam("email") String email) {
        boolean emailSent = passwordResetTokenService.sendPasswordResetEmail(email);
        if (emailSent) {
            return ResponseHelper.getResponse(messageUtil.getMessage("password.reset.email.sent"), HttpStatus.OK);
        } else {
            return ResponseHelper.getErrorResponse(messageUtil.getMessage("user.notfound"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public Object resetPassword(@RequestParam("token") String token,
                                @RequestBody PasswordResetRequest resetRequest) {
        boolean passwordReset = passwordResetTokenService.resetPassword(token, resetRequest);
        if (passwordReset) {
            return ResponseHelper.getResponse(messageUtil.getMessage("password.reset.successfully"), HttpStatus.OK);
        } else {
            return ResponseHelper.getErrorResponse(messageUtil.getMessage("invalid.or.expired.token"), HttpStatus.BAD_REQUEST);
        }
    }

}
