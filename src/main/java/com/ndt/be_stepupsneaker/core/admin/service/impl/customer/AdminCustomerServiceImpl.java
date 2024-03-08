package com.ndt.be_stepupsneaker.core.admin.service.impl.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyGrowthResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminCustomerMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminCustomerService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.core.common.base.Statistic;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.DailyStatisticUtil;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import com.ndt.be_stepupsneaker.util.RandomStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminCustomerServiceImpl implements AdminCustomerService {

    private CloudinaryUpload cloudinaryUpload;

    private AdminCustomerRepository adminCustomerRepository;

    private PaginationUtil paginationUtil;

    private EmailService emailService;

    private PasswordEncoder passwordEncoder;

    private AdminEmployeeRepository adminEmployeeRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    public AdminCustomerServiceImpl(CloudinaryUpload cloudinaryUpload,
                                    AdminCustomerRepository adminCustomerRepository,
                                    PaginationUtil paginationUtil,
                                    EmailService emailService,
                                    PasswordEncoder passwordEncoder,
                                    AdminEmployeeRepository adminEmployeeRepository
    ) {
        this.cloudinaryUpload = cloudinaryUpload;
        this.adminCustomerRepository = adminCustomerRepository;
        this.paginationUtil = paginationUtil;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.adminEmployeeRepository = adminEmployeeRepository;
    }


    @Override
    public PageableObject<AdminCustomerResponse> findAllCustomer(AdminCustomerRequest customerRequest, String voucher, String noVoucher) {
        Pageable pageable = paginationUtil.pageable(customerRequest);
        Page<Customer> resp = adminCustomerRepository.findAllCustomer(customerRequest, voucher, noVoucher, customerRequest.getStatus(), pageable);
        Page<AdminCustomerResponse> adminCustomerResponses = resp.map(AdminCustomerMapper.INSTANCE::customerToAdminCustomerResponse);

        return new PageableObject<>(adminCustomerResponses);
    }

    @Override
    public AdminDailyStatisticResponse getDailyCustomersBetween(Long start, Long end) {
        List<Statistic> statistics = adminCustomerRepository.getDailyCustomerBetween(start, end);
        return DailyStatisticUtil.getDailyStatisticResponse(statistics);
    }

    @Override
    public List<AdminDailyGrowthResponse> getCustomersGrowthBetween(Long start, Long end) {
        List<Statistic> statistics = adminCustomerRepository.getDailyCustomerBetween(start, end);
        return DailyStatisticUtil.getDailyGrowth(statistics);
    }

    @Override
    public PageableObject<AdminCustomerResponse> findAllEntity(AdminCustomerRequest request) {
        return null;
    }

    @Override
    public Object create(AdminCustomerRequest customerDTO) {
        Optional<Customer> customerOptional = adminCustomerRepository.findByEmail(customerDTO.getEmail());
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmail(customerDTO.getEmail());
        if (customerOptional.isPresent() || employeeOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.email.exist"));
        }
        String passWordRandom = RandomStringUtil.generateRandomPassword(6);
        customerDTO.setPassword(passwordEncoder.encode(passWordRandom));
        customerDTO.setImage(cloudinaryUpload.upload(customerDTO.getImage()));
        Customer customer = adminCustomerRepository.save(AdminCustomerMapper.INSTANCE.adminCustomerRequestToCustomer(customerDTO));
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoPassWord(customer, passWordRandom, null);
        return AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(customer);
    }

    @Override
    public AdminCustomerResponse update(AdminCustomerRequest customerDTO) {

        Optional<Customer> customerOptional = adminCustomerRepository.findByEmail(customerDTO.getId(), customerDTO.getEmail());
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmail(customerDTO.getEmail());
        if (customerOptional.isPresent() || employeeOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.email.exist"));
        }
        customerOptional = adminCustomerRepository.findById(customerDTO.getId());
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("customer.notfound"));
        }
        Customer customer = customerOptional.get();
        customer.setFullName(customerDTO.getFullName());
        customer.setStatus(customerDTO.getStatus());
        customer.setGender(customerDTO.getGender());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setEmail(customerDTO.getEmail());
        customer.setImage(cloudinaryUpload.upload(customerDTO.getImage()));
        return AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(adminCustomerRepository.save(customer));

    }

    @Override
    public AdminCustomerResponse findById(String id) {
        Optional<Customer> customerOptional = adminCustomerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("customer.notfound"));
        }
        return AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(customerOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Customer> customerOptional = adminCustomerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("customer.notfound"));
        }
        Customer customer = customerOptional.get();
        customer.setDeleted(true);
        adminCustomerRepository.save(customer);
        return true;
    }
}
