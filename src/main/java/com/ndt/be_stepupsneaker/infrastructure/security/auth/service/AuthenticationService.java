package com.ndt.be_stepupsneaker.infrastructure.security.auth.service;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.mapper.empolyee.AdminEmployeeMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminRoleRepository;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.AuthenticationResponse;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.AuthenticationRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.RegisterRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.config.JwtService;
import com.ndt.be_stepupsneaker.repository.employee.EmployeeRepository;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private final AdminCustomerRepository adminCustomerRepository;
    private final CloudinaryUpload cloudinaryUpload;

    public AuthenticationResponse register(AdminEmployeeRequest request) {
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmail(request.getEmail());
        if (employeeOptional.isPresent()) {
            throw new ApiException("Email is exist");
        }
        employeeOptional = adminEmployeeRepository.findByPhoneNumber(request.getPhoneNumber());
        if (employeeOptional.isPresent()) {
            throw new ApiException("PhoneNumber is exist");
        }
//        request.setImage(cloudinaryUpload.upload(request.getImage()));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Employee employee = adminEmployeeRepository.save(AdminEmployeeMapper.INSTANCE.adminEmployeeResquestToEmPolyee(request));
        adminEmployeeRepository.save(employee);
        var jwtToken = jwtService.generateToken(employee);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Employee employee = adminEmployeeRepository.findByEmail(request.getEmail()).orElse(null);
        Customer customer = adminCustomerRepository.findByEmail(request.getEmail()).orElse(null);
        String jwtToken;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassWord()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new ApiException("Invalid credentials!");
        }
        if (employee != null && passwordEncoder.matches(request.getPassWord(), employee.getPassword())) {
            jwtToken = jwtService.generateToken(employee);
        } else if (customer != null && passwordEncoder.matches(request.getPassWord(), customer.getPassword())) {
            jwtToken = jwtService.generateToken(customer);
        } else {
            throw new ApiException("Invalid credentials!");
        }
//        Cookie cookie = new Cookie("jwt", jwtToken);
//        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
