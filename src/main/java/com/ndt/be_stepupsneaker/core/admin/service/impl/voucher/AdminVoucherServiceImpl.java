package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.repository.voucher.CustomerVoucherRepository;
import com.ndt.be_stepupsneaker.util.ConvertTime;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminVoucherServiceImpl implements AdminVoucherService {
    @Qualifier("adminVoucherRepository")
    private AdminVoucherRepository adminVoucherRepository;

    private AdminOrderRepository adminOrderRepository;

    private PaginationUtil paginationUtil;

    private CustomerVoucherRepository customerVoucherRepository;
    @Autowired
    public AdminVoucherServiceImpl(
            AdminVoucherRepository adminVoucherRepository,
            CustomerVoucherRepository customerVoucherRepository,
            AdminOrderRepository adminOrderRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminVoucherRepository = adminVoucherRepository;
        this.customerVoucherRepository = customerVoucherRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.paginationUtil = paginationUtil;
    }


    @Override
    public PageableObject<AdminVoucherResponse> findAllEntity(AdminVoucherRequest voucherRequest) {

        Pageable pageable = paginationUtil.pageable(voucherRequest);
        Page<Voucher> resp = adminVoucherRepository.findAllVoucher(voucherRequest, pageable,voucherRequest.getStatus(),voucherRequest.getType());
        Page<AdminVoucherResponse> adminVoucherResponsePage = resp.map(AdminVoucherMapper.INSTANCE::voucherToAdminVoucherResponse);
        return new PageableObject<>(adminVoucherResponsePage);
    }

    @Override
    public AdminVoucherResponse create(AdminVoucherRequest voucherRequest) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findByCode(voucherRequest.getCode());
        if (optionalVoucher.isPresent()) {
            throw new ApiException("CODE IS EXIST");
        }
        Voucher voucher = adminVoucherRepository.save(AdminVoucherMapper.INSTANCE.adminVoucherRequestToVoucher(voucherRequest));

        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(voucher);
    }

    @Override
    public AdminVoucherResponse update(AdminVoucherRequest voucherRequest) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findByCode(voucherRequest.getId(), voucherRequest.getCode());
        if (optionalVoucher.isPresent()) {
            throw new ApiException("CODE IS EXIST");
        }

        optionalVoucher = adminVoucherRepository.findById(voucherRequest.getId());
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException("VOUCHER IS NOT EXIST");
        }
        Voucher newVoucher = optionalVoucher.get();
        newVoucher.setName(voucherRequest.getName());
        newVoucher.setCode(voucherRequest.getCode());
        newVoucher.setConstraint(voucherRequest.getConstraint());
        newVoucher.setQuantity(voucherRequest.getQuantity());
        newVoucher.setStatus(voucherRequest.getStatus());
        newVoucher.setImage(voucherRequest.getImage());
        newVoucher.setEndDate(voucherRequest.getEndDate());
        newVoucher.setStartDate(voucherRequest.getStartDate());
        newVoucher.setType(voucherRequest.getType());
        newVoucher.setValue(voucherRequest.getValue());
        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(adminVoucherRepository.save(newVoucher));
    }

    @Override
    public AdminVoucherResponse findById(UUID id) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findById(id);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException("VOUCHER IS NOT EXIST :" + id);
        }

        return AdminVoucherMapper.INSTANCE.voucherToAdminVoucherResponse(optionalVoucher.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Voucher> optionalVoucher = adminVoucherRepository.findById(id);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException("VOUCHER NOT FOUND :" + id);
        }
        Voucher newVoucher = optionalVoucher.get();
        newVoucher.setDeleted(true);
        adminVoucherRepository.save(newVoucher);
        return true;
    }


    @Override
    public void updateVoucherStatusAutomatically() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Voucher> voucherList = adminVoucherRepository.findAll();

        for (Voucher voucherStatus : voucherList) {
            if (ConvertTime.convertLocalDateTimeToLong(currentDateTime) < (voucherStatus.getStartDate())) {
                voucherStatus.setStatus(VoucherStatus.IN_ACTIVE);
            } else if (ConvertTime.convertLocalDateTimeToLong(currentDateTime) >= (voucherStatus.getStartDate()) && ConvertTime.convertLocalDateTimeToLong(currentDateTime) <= (voucherStatus.getEndDate())) {
                voucherStatus.setStatus(VoucherStatus.ACTIVE);
            } else {
                voucherStatus.setStatus(VoucherStatus.EXPIRED);
            }

            adminVoucherRepository.save(voucherStatus);
        }
    }

    @Override
    public void updateOrderAutomatically() {

        long currentMillis = Instant.now().toEpochMilli();
        // Calculate the time 30 minutes ago in milliseconds
        long thirtyMinutesAgo = currentMillis - (30 * 60 * 1000); // 30 minutes * 60 seconds/minute * 1000 milliseconds/second

        List<Order> expiredOrders = adminOrderRepository.findAllByStatusAndCreatedAtBefore(OrderStatus.PENDING, thirtyMinutesAgo);

        if (!expiredOrders.isEmpty()){
            adminOrderRepository.deleteAllInBatch(expiredOrders);
        }
    }


}
