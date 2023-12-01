package com.ndt.be_stepupsneaker.infrastructure.security.auth.controller;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.AuthenticationResponse;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.AuthenticationRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.service.AuthenticationService;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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

}
