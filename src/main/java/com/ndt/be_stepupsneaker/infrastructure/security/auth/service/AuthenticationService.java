package com.ndt.be_stepupsneaker.infrastructure.security.auth.service;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminRoleRepository;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.AuthenticationResponse;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.AuthenticationRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.RegisterRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.config.JwtService;
import com.ndt.be_stepupsneaker.repository.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AdminRoleRepository adminRoleRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var employee = Employee.builder()
                .address(request.getAddress())
                .email(request.getEmail())
                .gender(request.getGender())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassWord()))
                .role(adminRoleRepository.findById(request.getRole())
                        .orElseThrow(() -> new ResourceNotFoundException("Role Not Found!")))
                .build();
        adminEmployeeRepository.save(employee);
        var jwtToken = jwtService.generateToken(employee);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassWord()
                )
        );
        var employee = adminEmployeeRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(employee);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
