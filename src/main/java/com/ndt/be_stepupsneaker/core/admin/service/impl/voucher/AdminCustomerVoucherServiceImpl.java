package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminCustomerMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminCustomerVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminCustomerVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminCustomerVoucherServiceImpl implements AdminCustomerVoucherService {

    private AdminCustomerVoucherRepository adminCustomerVoucherRepository;
    private PaginationUtil paginationUtil;

    @Autowired
    public AdminCustomerVoucherServiceImpl(AdminCustomerVoucherRepository adminCustomerVoucherRepository, PaginationUtil paginationUtil) {
        this.adminCustomerVoucherRepository = adminCustomerVoucherRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminCustomerVoucherResponse> findAllEntity(AdminCustomerVoucherRequest customerVoucherRequest) {

        Pageable pageable = paginationUtil.pageable(customerVoucherRequest);
        Page<CustomerVoucher> resp = adminCustomerVoucherRepository.findAllCustomerVoucher(customerVoucherRequest, pageable);
        Page<AdminCustomerVoucherResponse> adminCustomerVoucherResponsePage = resp.map(AdminCustomerVoucherMapper.INSTANCE::customerVoucherToAdminCustomerVoucherResponse);
        return new PageableObject<>(adminCustomerVoucherResponsePage);
    }

    @Override
    public AdminCustomerVoucherResponse create(AdminCustomerVoucherRequest CustomerVoucherRequest) {
        return null;
    }

    @Override
    public AdminCustomerVoucherResponse update(AdminCustomerVoucherRequest CustomerVoucherRequest) {
        return null;
    }

    @Override
    public AdminCustomerVoucherResponse findById(UUID id) {
        Optional<CustomerVoucher> optionalCustomerVoucher = adminCustomerVoucherRepository.findById(id);
        if (optionalCustomerVoucher.isEmpty()) {
            throw new ResourceNotFoundException("CustomerVoucher IS NOT EXIST :" + id);
        }

        return AdminCustomerVoucherMapper.INSTANCE.customerVoucherToAdminCustomerVoucherResponse(optionalCustomerVoucher.get());
    }

    @Override
    public Boolean delete(UUID id) {
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
    public List<AdminCustomerVoucherResponse> createCustomerVoucher(List<AdminVoucherRequest> voucherRequestList, List<AdminCustomerRequest> adminCustomerRequests) {
        List<AdminCustomerVoucherResponse> adminCustomerVoucherResponseList = new ArrayList<>();
        for (AdminVoucherRequest voucherRequest : voucherRequestList) {
            for (AdminCustomerRequest adminCustomerRequest : adminCustomerRequests) {
                CustomerVoucher newCustomerVoucher = new CustomerVoucher();
                newCustomerVoucher.setCustomer(AdminCustomerMapper.INSTANCE.adminCustomerRequestToCustomer(adminCustomerRequest));
                newCustomerVoucher.setVoucher(AdminVoucherMapper.INSTANCE.adminVoucherRequestToVoucher(voucherRequest));
                System.out.println("========" + newCustomerVoucher);
                adminCustomerVoucherRepository.save(newCustomerVoucher);
                adminCustomerVoucherResponseList.add(AdminCustomerVoucherMapper.INSTANCE.customerVoucherToAdminCustomerVoucherResponse(newCustomerVoucher));

            }
        }
        return adminCustomerVoucherResponseList;
    }

    @Override
    public PageableObject<AdminCustomerResponse> getAllCustomerByVoucherId(UUID id, AdminCustomerRequest customerRequest) {
        Pageable pageable = paginationUtil.pageable(customerRequest);
        Page<Customer> resp = adminCustomerVoucherRepository.getAllCustomerByVoucherId(id, customerRequest.getStatus(), customerRequest, pageable);
        Page<AdminCustomerResponse> adminCustomerVoucherRespPage = resp.map(AdminCustomerMapper.INSTANCE::customerToAdminCustomerResponse);
        return new PageableObject<>(adminCustomerVoucherRespPage);
    }

    @Override
    public PageableObject<AdminCustomerResponse> getAllCustomerNotInVoucherId(UUID id, AdminCustomerRequest customerRequest) {
        Pageable pageable = paginationUtil.pageable(customerRequest);
        Page<Customer> resp = adminCustomerVoucherRepository.getAllCustomerNotInVoucherId(id, customerRequest.getStatus(), customerRequest, pageable);
        Page<AdminCustomerResponse> adminCustomerVoucherRespPage = resp.map(AdminCustomerMapper.INSTANCE::customerToAdminCustomerResponse);
        return new PageableObject<>(adminCustomerVoucherRespPage);
    }

        @Override
        public Boolean deleteCustomersByVoucherIdAndCustomerIds(UUID voucherId, List<UUID> customerIds) {
            adminCustomerVoucherRepository.deleteCustomersByVoucherIdAndCustomerIds(voucherId, customerIds);
            return true;
        }


}
