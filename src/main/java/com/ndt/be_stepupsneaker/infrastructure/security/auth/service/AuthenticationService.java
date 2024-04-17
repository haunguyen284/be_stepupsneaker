package com.ndt.be_stepupsneaker.infrastructure.security.auth.service;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.empolyee.AdminEmployeeMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientCustomerMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.EmployeeStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.AuthenticationResponse;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.AuthenticationRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.ChangePasswordRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.config.JwtService;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
    private final ClientCustomerRepository clientCustomerRepository;
    private final CloudinaryUpload cloudinaryUpload;
    private final MySessionInfo mySessionInfo;
    private final MessageUtil messageUtil;

    public AuthenticationResponse registerEmployee(AdminEmployeeRequest request) {
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmailAndDeleted(request.getEmail(),false);
        if (employeeOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.email.exist"));
        }
        employeeOptional = adminEmployeeRepository.findByPhoneNumber(request.getPhoneNumber());
        if (employeeOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.phone.exist"));
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Employee employee = adminEmployeeRepository.save(AdminEmployeeMapper.INSTANCE.adminEmployeeResquestToEmPolyee(request));
        employee.setStatus(EmployeeStatus.ACTIVE);
        adminEmployeeRepository.save(employee);
        var jwtToken = jwtService.generateToken(employee);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse registerCustomer(ClientCustomerRequest request) {
        Optional<Customer> customerOptional = clientCustomerRepository.findByEmailAndDeleted(request.getEmail(),false);
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmailAndDeleted(request.getEmail(),false);
        if (customerOptional.isPresent() || employeeOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.email.exist"));
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Customer customer = clientCustomerRepository.save(ClientCustomerMapper.INSTANCE.clientCustomerRequestToCustomer(request));
        customer.setStatus(CustomerStatus.ACTIVE);
        clientCustomerRepository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Employee employee = adminEmployeeRepository.findByEmailAndDeleted(request.getEmail(),false).orElse(null);
        Customer customer = clientCustomerRepository.findByEmailAndDeleted(request.getEmail(),false).orElse(null);
        String jwtToken;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new ApiException(messageUtil.getMessage("username.or.password.incorrect"));
        }
        if (employee != null && passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            jwtToken = jwtService.generateToken(employee);
        } else if (customer != null && passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            jwtToken = jwtService.generateToken(customer);
        } else {
            throw new ApiException(messageUtil.getMessage("username.or.password.incorrect"));
        }
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Object changePassword(ChangePasswordRequest request) {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        AdminEmployeeResponse employeeResponse = mySessionInfo.getCurrentEmployee();
        if (employeeResponse != null) {
            Employee employee = adminEmployeeRepository.findById(employeeResponse.getId()).orElse(null);
            if (employee != null) {
                passwordCompare(request, employee.getPassword());
                employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
                return AdminEmployeeMapper.INSTANCE.employeeToAdminEmpolyeeResponse(adminEmployeeRepository.save(employee));
            }
        }
        Customer customer = clientCustomerRepository.findById(customerResponse.getId()).orElse(null);
        passwordCompare(request, customer.getPassword());
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return ClientCustomerMapper.INSTANCE.customerToClientCustomerResponse(clientCustomerRepository.save(customer));

    }

    public void passwordCompare(ChangePasswordRequest request, String passwordUser) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), passwordUser)) {
            throw new ApiException(messageUtil.getMessage("current.password.is.incorrect"));
        } else if (!request.getEnterThePassword().equals(request.getNewPassword())) {
            throw new ApiException(messageUtil.getMessage("re_entered.password.in.incorrect"));
        }
    }
}
