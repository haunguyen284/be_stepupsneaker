package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminCustomerVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminCustomerVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminCustomerVoucherServiceImpl implements AdminCustomerVoucherService {

    private AdminCustomerVoucherRepository adminCustomerVoucherRepository;
    private PaginationUtil paginationUtil;
    private AdminCustomerRepository adminCustomerRepository;
    private AdminVoucherRepository adminVoucherRepository;
    private EmailService emailService;

    @Autowired
    public AdminCustomerVoucherServiceImpl(AdminCustomerVoucherRepository adminCustomerVoucherRepository,
                                           PaginationUtil paginationUtil,
                                           AdminCustomerRepository adminCustomerRepository,
                                           AdminVoucherRepository adminVoucherRepository,
                                           EmailService emailService) {
        this.adminCustomerVoucherRepository = adminCustomerVoucherRepository;
        this.paginationUtil = paginationUtil;
        this.adminCustomerRepository = adminCustomerRepository;
        this.adminVoucherRepository = adminVoucherRepository;
        this.emailService = emailService;
    }

    @Override
    public PageableObject<AdminCustomerVoucherResponse> findAllEntity(AdminCustomerVoucherRequest customerVoucherRequest) {

        Pageable pageable = paginationUtil.pageable(customerVoucherRequest);
        Page<CustomerVoucher> resp = adminCustomerVoucherRepository.findAllCustomerVoucher(customerVoucherRequest, pageable);
        Page<AdminCustomerVoucherResponse> adminCustomerVoucherResponsePage = resp.map(AdminCustomerVoucherMapper.INSTANCE::customerVoucherToAdminCustomerVoucherResponse);
        return new PageableObject<>(adminCustomerVoucherResponsePage);
    }

    @Override
    public Object create(AdminCustomerVoucherRequest CustomerVoucherRequest) {
        return null;
    }

    @Override
    public AdminCustomerVoucherResponse update(AdminCustomerVoucherRequest CustomerVoucherRequest) {
        return null;
    }

    @Override
    public AdminCustomerVoucherResponse findById(String id) {
        Optional<CustomerVoucher> optionalCustomerVoucher = adminCustomerVoucherRepository.findById(id);
        if (optionalCustomerVoucher.isEmpty()) {
            throw new ResourceNotFoundException("CustomerVoucher IS NOT EXIST :" + id);
        }
        return AdminCustomerVoucherMapper.INSTANCE.customerVoucherToAdminCustomerVoucherResponse(optionalCustomerVoucher.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<CustomerVoucher> optionalCustomerVoucher = adminCustomerVoucherRepository.findById(id);
        if (optionalCustomerVoucher.isEmpty()) {
            throw new ResourceNotFoundException("CustomerVoucher NOT FOUND :" + id);
        }
        CustomerVoucher newCustomerVoucher = optionalCustomerVoucher.get();
        newCustomerVoucher.setDeleted(true);
        adminCustomerVoucherRepository.save(newCustomerVoucher);
        return true;
    }

    @Override
    public List<AdminCustomerVoucherResponse> createCustomerVoucher(List<String> voucherIds, List<String> customerIds) {
        List<AdminCustomerVoucherResponse> adminCustomerVoucherResponseList = new ArrayList<>();
        if (customerIds == null || customerIds.isEmpty() || customerIds.size() == 0) {
            List<Customer> customers = adminCustomerRepository.getAllByDeleted();
            customers.stream().forEach(customer -> {
                customerIds.add(customer.getId());
            });
        }
        for (String voucherId : voucherIds) {
            for (String customerId : customerIds) {
                Optional<Voucher> optionalVoucher = adminVoucherRepository.findById(voucherId);
                Optional<Customer> optionalCustomer = adminCustomerRepository.findById(customerId);
                if (optionalVoucher.isEmpty() || optionalCustomer.isEmpty()) {
                    throw new ResourceNotFoundException("VOUCHER OR CUSTOMER NOT FOUND !");
                }
                CustomerVoucher newCustomerVoucher = new CustomerVoucher();
                newCustomerVoucher.setVoucher(optionalVoucher.get());
                newCustomerVoucher.setCustomer(optionalCustomer.get());
                CustomerVoucher savedCustomerVoucher = adminCustomerVoucherRepository.save(newCustomerVoucher);
//                SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
//                sendMailAutoEntity.sendMailAutoVoucherToCustomer(savedCustomerVoucher);
                adminCustomerVoucherResponseList.add(AdminCustomerVoucherMapper.INSTANCE.customerVoucherToAdminCustomerVoucherResponse(savedCustomerVoucher));

            }
        }
        return adminCustomerVoucherResponseList;
    }

    @Override
    public Boolean deleteCustomersByVoucherIdAndCustomerIds(String voucherId, List<String> customerIds) {
        adminCustomerVoucherRepository.deleteCustomersByVoucherIdAndCustomerIds(voucherId, customerIds);
        return true;
    }


}
