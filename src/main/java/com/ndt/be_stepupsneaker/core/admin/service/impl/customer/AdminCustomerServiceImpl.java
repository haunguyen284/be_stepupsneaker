package com.ndt.be_stepupsneaker.core.admin.service.impl.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminCustomerMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminCustomerService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminCustomerServiceImpl implements AdminCustomerService {

    private AdminCustomerRepository adminCustomerRepository;

    private PaginationUtil paginationUtil;

    private EmailService emailService;

    @Autowired
    public AdminCustomerServiceImpl(AdminCustomerRepository adminCustomerRepository, PaginationUtil paginationUtil, EmailService emailService) {
        this.adminCustomerRepository = adminCustomerRepository;
        this.paginationUtil = paginationUtil;
        this.emailService = emailService;
    }


    @Override
    public PageableObject<AdminCustomerResponse> findAllCustomer(AdminCustomerRequest customerRequest, UUID voucherId, UUID noVoucherId) {
        Pageable pageable = paginationUtil.pageable(customerRequest);
        Page<Customer> resp = adminCustomerRepository.findAllCustomer(customerRequest, voucherId, noVoucherId, customerRequest.getStatus(), pageable);
        Page<AdminCustomerResponse> adminCustomerResponses = resp.map(AdminCustomerMapper.INSTANCE::customerToAdminCustomerResponse);

        return new PageableObject<>(adminCustomerResponses);
    }

    @Override
    public PageableObject<AdminCustomerResponse> findAllEntity(AdminCustomerRequest request) {
        return null;
    }

    @Override
    public AdminCustomerResponse create(AdminCustomerRequest customerDTO) {
        Optional<Customer> customerOptional = adminCustomerRepository.findByEmail(customerDTO.getEmail());
        if (customerOptional.isPresent()) {
            throw new ApiException("Email is exit");
        }
        customerDTO.setPassword("admin123");
        Customer customer = adminCustomerRepository.save(AdminCustomerMapper.INSTANCE.adminCustomerRequestToCustomer(customerDTO));
        String[] toEmail = new String[1];
        toEmail[0] = customer.getEmail();
        Email email = new Email();
        email.setToEmail(toEmail);
        email.setSubject("STEP UP SNEAKER send your password ...");
        email.setTitleEmail("STEP UP SNEAKER send mail your password : \n" +
                "\n");
        email.setBody("\n" + customer.getPassword());
        emailService.sendEmail(email);
        return AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(customer);
    }

    @Override
    public AdminCustomerResponse update(AdminCustomerRequest customerDTO) {

        Optional<Customer> customerOptional = adminCustomerRepository.findByEmail(customerDTO.getId(), customerDTO.getEmail());
        if (customerOptional.isPresent()) {
            throw new ApiException("Email is exit");
        }
        customerOptional = adminCustomerRepository.findById(customerDTO.getId());
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Customer is not exits");
        }
        Customer customer = customerOptional.get();
        customer.setFullName(customerDTO.getFullName());
        customer.setStatus(customerDTO.getStatus());
        customer.setGender(customerDTO.getGender());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setEmail(customerDTO.getEmail());
        customer.setImage(customerDTO.getImage());
        return AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(adminCustomerRepository.save(customer));

    }

    @Override
    public AdminCustomerResponse findById(UUID id) {
        Optional<Customer> customerOptional = adminCustomerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("LOOI");
        }
        return AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(customerOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Customer> customerOptional = adminCustomerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found");
        }
        Customer customer = customerOptional.get();
        customer.setDeleted(true);
        adminCustomerRepository.save(customer);
        return true;
    }
}
