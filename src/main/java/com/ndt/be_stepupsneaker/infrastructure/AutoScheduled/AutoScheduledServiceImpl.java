package com.ndt.be_stepupsneaker.infrastructure.AutoScheduled;

import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.util.ConvertTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutoScheduledServiceImpl implements AutoScheduledService{
    private AdminVoucherRepository adminVoucherRepository;
    private AdminOrderRepository adminOrderRepository;


    @Autowired
    public AutoScheduledServiceImpl(AdminVoucherRepository adminVoucherRepository, AdminOrderRepository adminOrderRepository) {
        this.adminVoucherRepository = adminVoucherRepository;
        this.adminOrderRepository = adminOrderRepository;
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
