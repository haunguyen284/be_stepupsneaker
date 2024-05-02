package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminPromotionMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminCustomerVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.EntityUtil;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminVoucherServiceImpl implements AdminVoucherService {


    @Qualifier("adminVoucherRepository")
    private final AdminVoucherRepository adminVoucherRepository;
    private final PaginationUtil paginationUtil;
    private final CloudinaryUpload cloudinaryUpload;
    private final MessageUtil messageUtil;
    private final AdminCustomerRepository adminCustomerRepository;
    private final EntityUtil entityUtil;

    @Override
    public PageableObject<AdminVoucherResponse> findAllEntity(AdminVoucherRequest voucherRequest) {
        return null;
    }

    @Override
    public Object create(AdminVoucherRequest voucherRequest) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findByCode(voucherRequest.getCode());
        if (optionalVoucher.isPresent()) {
            throw new ApiException(messageUtil.getMessage("voucher.code.exist"));
        }
        if (voucherRequest.getType() == VoucherType.PERCENTAGE) {
            if (voucherRequest.getValue() > 70) {
                throw new ApiException(messageUtil.getMessage("voucher.value.max"));
            }
        }
        voucherRequest.setImage(cloudinaryUpload.upload(voucherRequest.getImage()));
        Voucher voucher = adminVoucherRepository.save(AdminVoucherMapper.INSTANCE.adminVoucherRequestToVoucher(voucherRequest));
        adminVoucherRepository.updateStatusBasedOnTime(voucher.getId(), voucher.getStartDate(), voucher.getEndDate());
        if (voucher != null) {
            List<String> customerIds = voucherRequest.getCustomers();
            if (voucherRequest.getCustomers() == null || voucherRequest.getCustomers().isEmpty()) {
                List<Customer> customers = adminCustomerRepository.getAllByDeleted();
                customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
            }
            entityUtil.addCustomersToVoucher(voucher, customerIds);
        }
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(voucher);
    }

    @Override
    public AdminVoucherResponse update(AdminVoucherRequest voucherRequest) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findByCode(voucherRequest.getId(), voucherRequest.getCode());
        if (optionalVoucher.isPresent()) {
            throw new ApiException(messageUtil.getMessage("voucher.code.exist"));
        }
        Voucher newVoucher = getVoucher(voucherRequest.getId());
        newVoucher.setName(voucherRequest.getName());
        newVoucher.setCode(voucherRequest.getCode());
        newVoucher.setConstraint(voucherRequest.getConstraint());
        newVoucher.setQuantity(voucherRequest.getQuantity());
        newVoucher.setImage(cloudinaryUpload.upload(voucherRequest.getImage()));
        newVoucher.setEndDate(voucherRequest.getEndDate());
        newVoucher.setStartDate(voucherRequest.getStartDate());
        newVoucher.setType(voucherRequest.getType());
        if (voucherRequest.getType() == VoucherType.PERCENTAGE) {
            if (voucherRequest.getValue() > 70) {
                throw new ApiException(messageUtil.getMessage("voucher.value.max"));
            }
        }
        newVoucher.setValue(voucherRequest.getValue());
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(adminVoucherRepository.save(newVoucher));
    }

    @Override
    public AdminVoucherResponse findById(String id) {
         Voucher voucher = getVoucher(id);
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(voucher);
    }

    @Override
    public Boolean delete(String id) {
        Voucher newVoucher = getVoucher(id);
        newVoucher.setDeleted(true);
        adminVoucherRepository.save(newVoucher);
        return true;
    }


    @Override
    public PageableObject<AdminVoucherResponse> findAllVoucher(AdminVoucherRequest voucherReq, String customerId, String noCustomerId) {
        Pageable pageable = paginationUtil.pageable(voucherReq);
        Page<Voucher> resp = adminVoucherRepository.findAllVoucher(voucherReq, pageable, voucherReq.getStatus(), voucherReq.getType(), customerId, noCustomerId);
        Page<AdminVoucherResponse> adminVoucherResponsePage = resp.map(AdminVoucherMapper.INSTANCE::voucherToAdminVoucherResponse);
        return new PageableObject<>(adminVoucherResponsePage);
    }

    @Override
    public AdminVoucherResponse deactivateDiscount(String id) {
        Voucher voucher = getVoucher(id);
        if (voucher.getStatus() == VoucherStatus.EXPIRED
                || voucher.getStatus() == VoucherStatus.IN_ACTIVE
                || voucher.getStatus() == VoucherStatus.UP_COMING) {
            throw new ApiException(messageUtil.getMessage("voucher.status"));
        }
        if (voucher.getStatus() == VoucherStatus.ACTIVE) {
            voucher.setStatus(VoucherStatus.CANCELLED);
        } else {
            voucher.setStatus(VoucherStatus.ACTIVE);
        }
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(adminVoucherRepository.save(voucher));
    }

    private Voucher getVoucher(String id) {
        Voucher voucher = adminVoucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound")));
        return voucher;
    }

}
