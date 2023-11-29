package com.ndt.be_stepupsneaker.core.client.service.impl.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientCustomerMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.service.customer.ClientCustomerService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import com.ndt.be_stepupsneaker.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientCustomerServiceImpl implements ClientCustomerService {


    private final CloudinaryUpload cloudinaryUpload;

    private final ClientCustomerRepository clientCustomerRepository;

    private final PaginationUtil paginationUtil;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;


    @Override
    public PageableObject<ClientCustomerResponse> findAllCustomer(ClientCustomerRequest customerRequest, String voucher, String noVoucher) {
        Pageable pageable = paginationUtil.pageable(customerRequest);
        Page<Customer> resp = clientCustomerRepository.findAllCustomer(customerRequest, voucher, noVoucher, customerRequest.getStatus(), pageable);
        Page<ClientCustomerResponse> ClientCustomerResponses = resp.map(ClientCustomerMapper.INSTANCE::customerToClientCustomerResponse);

        return new PageableObject<>(ClientCustomerResponses);
    }


    @Override
    public PageableObject<ClientCustomerResponse> findAllEntity(ClientCustomerRequest request) {
        return null;
    }

    @Override
    public Object create(ClientCustomerRequest customerDTO) {
        Optional<Customer> customerOptional = clientCustomerRepository.findByEmail(customerDTO.getEmail());
        if (customerOptional.isPresent()) {
            throw new ApiException("EMAIL IS EXIT !");
        }
        String passWordRandom = RandomStringUtil.generateRandomPassword(6);
        customerDTO.setPassword(passwordEncoder.encode(passWordRandom));
        customerDTO.setImage(cloudinaryUpload.upload(customerDTO.getImage()));
        Customer customer = clientCustomerRepository.save(ClientCustomerMapper.INSTANCE.clientCustomerRequestToCustomer(customerDTO));
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoPassWord(customer,passWordRandom,null);
        return ClientCustomerMapper.INSTANCE.customerToClientCustomerResponse(customer);
    }

    @Override
    public ClientCustomerResponse update(ClientCustomerRequest customerDTO) {

        Optional<Customer> customerOptional = clientCustomerRepository.findByEmail(customerDTO.getId(), customerDTO.getEmail());
        if (customerOptional.isPresent()) {
            throw new ApiException("Email is exit !");
        }
        customerOptional = clientCustomerRepository.findById(customerDTO.getId());
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Customer is not exits !");
        }
        Customer customer = customerOptional.get();
        customer.setFullName(customerDTO.getFullName());
        customer.setStatus(customerDTO.getStatus());
        customer.setGender(customerDTO.getGender());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setEmail(customerDTO.getEmail());
        customer.setImage(cloudinaryUpload.upload(customerDTO.getImage()));
        return ClientCustomerMapper.INSTANCE.customerToClientCustomerResponse(clientCustomerRepository.save(customer));

    }

    @Override
    public ClientCustomerResponse findById(String id) {
        Optional<Customer> customerOptional = clientCustomerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Customer Not Found !");
        }
        return ClientCustomerMapper.INSTANCE.customerToClientCustomerResponse(customerOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Customer> customerOptional = clientCustomerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found");
        }
        Customer customer = customerOptional.get();
        customer.setDeleted(true);
        clientCustomerRepository.save(customer);
        return true;
    }
}
