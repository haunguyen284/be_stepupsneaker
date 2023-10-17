package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminCustomerVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminCustomerVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminCustomerVoucherServiceImpl implements AdminCustomerVoucherService {
    @Autowired
    private AdminCustomerVoucherRepository adminCustomerVoucherRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminCustomerVoucherResponse> findAllEntity(AdminCustomerVoucherRequest CustomerVoucherRequest) {

        Pageable pageable = paginationUtil.pageable(CustomerVoucherRequest);
        Page<CustomerVoucher> resp = adminCustomerVoucherRepository.findAllCustomerVoucher(CustomerVoucherRequest, pageable);
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

}
