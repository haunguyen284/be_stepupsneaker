package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.scheduled.ScheduledService;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.repository.voucher.CustomerVoucherRepository;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminVoucherServiceImpl implements AdminVoucherService {


    @Qualifier("adminVoucherRepository")
    private final AdminVoucherRepository adminVoucherRepository;
    private final PaginationUtil paginationUtil;
    private final CloudinaryUpload cloudinaryUpload;
    private final MessageUtil messageUtil;


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
            if (voucherRequest.getValue() > 70){
                throw new ApiException(messageUtil.getMessage("voucher.value.max"));
            }
        }
        voucherRequest.setImage(cloudinaryUpload.upload(voucherRequest.getImage()));
        Voucher voucher = adminVoucherRepository.save(AdminVoucherMapper.INSTANCE.adminVoucherRequestToVoucher(voucherRequest));
        adminVoucherRepository.updateStatusBasedOnTime(voucher.getId(), voucher.getStartDate(), voucher.getEndDate());
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(voucher);
    }

    @Override
    public AdminVoucherResponse update(AdminVoucherRequest voucherRequest) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findByCode(voucherRequest.getId(), voucherRequest.getCode());
        if (optionalVoucher.isPresent()) {
            throw new ApiException(messageUtil.getMessage("voucher.code.exist"));
        }

        optionalVoucher = adminVoucherRepository.findById(voucherRequest.getId());
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
        }
        Voucher newVoucher = optionalVoucher.get();
        newVoucher.setName(voucherRequest.getName());
        newVoucher.setCode(voucherRequest.getCode());
        newVoucher.setConstraint(voucherRequest.getConstraint());
        newVoucher.setQuantity(voucherRequest.getQuantity());
        newVoucher.setStatus(voucherRequest.getStatus());
        newVoucher.setImage(cloudinaryUpload.upload(voucherRequest.getImage()));
        newVoucher.setEndDate(voucherRequest.getEndDate());
        newVoucher.setStartDate(voucherRequest.getStartDate());
        newVoucher.setType(voucherRequest.getType());
        if (voucherRequest.getType() == VoucherType.PERCENTAGE) {
            if (voucherRequest.getValue() > 70){
                throw new ApiException(messageUtil.getMessage("voucher.value.max"));
            }
        }
        newVoucher.setValue(voucherRequest.getValue());
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(adminVoucherRepository.save(newVoucher));
    }

    @Override
    public AdminVoucherResponse findById(String id) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findById(id);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
        }

        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(optionalVoucher.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findById(id);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
        }
        Voucher newVoucher = optionalVoucher.get();
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


}
