package com.ndt.be_stepupsneaker.infrastructure.security.session;

import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.empolyee.AdminEmployeeMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientCustomerMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class MySessionInfo {
    private final ClientCustomerRepository clientCustomerRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private Customer customer;
    private Employee employee;

    public ClientCustomerResponse getCurrentCustomer() {
        if (customer == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            customer = clientCustomerRepository.findByEmail(userName).orElse(null);
            if (customer != null) {
                List<CustomerVoucher> validCustomerVouchers = customer.getCustomerVoucherList().stream()
                        .filter(customerVoucher -> {
                            Voucher voucher = customerVoucher.getVoucher();
                            return
                                    voucher.getStatus().equals(VoucherStatus.ACTIVE)
                                            && voucher.getQuantity() > 0
                                            && !voucher.getDeleted();
                        })
                        .collect(Collectors.toList());
                List<Address> addresses = customer.getAddressList().stream()
                        .filter(address -> !address.getDeleted())
                        .collect(Collectors.toList());
                customer.setCustomerVoucherList(validCustomerVouchers);
                customer.setAddressList(addresses);
            }
        }
        return ClientCustomerMapper.INSTANCE.customerToClientCustomerResponse(customer);
    }

    public AdminEmployeeResponse getCurrentEmployee() {
        if (employee == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            employee = adminEmployeeRepository.findByEmail(userName).orElse(null);
        }
        return AdminEmployeeMapper.INSTANCE.employeeToAdminEmpolyeeResponse(employee);
    }
}
