package com.ndt.be_stepupsneaker.infrastructure.security.session;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class MySessionInfo {
    private final ClientCustomerRepository clientCustomerRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private Customer customer;
    private Employee employee;

    public Customer getCurrentCustomer() {
        if (customer == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            customer = clientCustomerRepository.findByEmail(userName).orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        }
        return customer;
    }

    public Employee getCurrentEmployee() {
        if (employee == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            employee = adminEmployeeRepository.findByEmail(userName).orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        }
        return employee;
    }
}
