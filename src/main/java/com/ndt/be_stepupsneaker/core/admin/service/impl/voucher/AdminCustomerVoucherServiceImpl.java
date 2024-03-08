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
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCustomerVoucherServiceImpl implements AdminCustomerVoucherService {

    private final AdminCustomerVoucherRepository adminCustomerVoucherRepository;
    private final PaginationUtil paginationUtil;
    private final AdminCustomerRepository adminCustomerRepository;
    private final AdminVoucherRepository adminVoucherRepository;
    private final EmailService emailService;
    private final MessageUtil messageUtil;


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
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.customer.notfound"));
        }
        return AdminCustomerVoucherMapper.INSTANCE.customerVoucherToAdminCustomerVoucherResponse(optionalCustomerVoucher.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<CustomerVoucher> optionalCustomerVoucher = adminCustomerVoucherRepository.findById(id);
        if (optionalCustomerVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.customer.notfound"));
        }
        CustomerVoucher newCustomerVoucher = optionalCustomerVoucher.get();
        newCustomerVoucher.setDeleted(true);
        adminCustomerVoucherRepository.save(newCustomerVoucher);
        return true;
    }

    @Override
    public List<AdminCustomerVoucherResponse> createCustomerVoucher(List<String> voucherIds, List<String> customerIds) {
        if (customerIds == null || customerIds.isEmpty()) {
            List<Customer> customers = adminCustomerRepository.getAllByDeleted();
            customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
        }
        List<CustomerVoucher> customerVouchers = new ArrayList<>();
        for (String voucherId : voucherIds) {
            for (String customerId : customerIds) {
                Optional<Voucher> optionalVoucher = adminVoucherRepository.findById(voucherId);
                Optional<Customer> optionalCustomer = adminCustomerRepository.findById(customerId);
                if (optionalVoucher.isEmpty() || optionalCustomer.isEmpty()) {
                    throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
                }
                CustomerVoucher newCustomerVoucher = new CustomerVoucher();
                newCustomerVoucher.setVoucher(optionalVoucher.get());
                newCustomerVoucher.setCustomer(optionalCustomer.get());
                customerVouchers.add(newCustomerVoucher);
            }
        }
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoVoucherToCustomer(customerVouchers);
        return adminCustomerVoucherRepository
                .saveAll(customerVouchers)
                .stream()
                .map(AdminCustomerVoucherMapper.INSTANCE::customerVoucherToAdminCustomerVoucherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteCustomersByVoucherIdAndCustomerIds(String voucherId, List<String> customerIds) {
        adminCustomerVoucherRepository.deleteCustomersByVoucherIdAndCustomerIds(voucherId, customerIds);
        return true;
    }


}
