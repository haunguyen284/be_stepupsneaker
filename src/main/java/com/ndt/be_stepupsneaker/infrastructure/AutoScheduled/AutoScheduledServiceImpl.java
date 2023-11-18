package com.ndt.be_stepupsneaker.infrastructure.AutoScheduled;

import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.util.ConvertTime;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AutoScheduledServiceImpl implements AutoScheduledService {
    private final AdminVoucherRepository adminVoucherRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final AdminPromotionRepository adminPromotionRepository;
    private final LocalDateTime currentDateTime = LocalDateTime.now();


    @Autowired
    public AutoScheduledServiceImpl(
            AdminVoucherRepository adminVoucherRepository,
            AdminOrderRepository adminOrderRepository,
            AdminOrderHistoryRepository adminOrderHistoryRepository,
            AdminOrderDetailRepository adminOrderDetailRepository,
            AdminPromotionRepository adminPromotionRepository
    ) {
        this.adminVoucherRepository = adminVoucherRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.adminOrderHistoryRepository = adminOrderHistoryRepository;
        this.adminOrderDetailRepository = adminOrderDetailRepository;
        this.adminPromotionRepository = adminPromotionRepository;
    }

    @Override
    public void updateVoucherStatusAutomatically() {
        Long currentLongTime = ConvertTime.convertLocalDateTimeToLong(currentDateTime);
        adminVoucherRepository.updateStatusAutomatically(currentLongTime);
    }
    @Override
    public void updatePromotionStatusAutomatically() {
        Long currentLongTime = ConvertTime.convertLocalDateTimeToLong(currentDateTime);
        adminPromotionRepository.updateStatusAutomatically(currentLongTime);
    }

    @Transactional
    @Override
    public void updateOrderAutomatically() {
        long currentMillis = Instant.now().toEpochMilli();
        // Calculate the time 30 minutes ago in milliseconds
        long thirtyMinutesAgo = currentMillis - (30 * 60 * 1000); // 30 minutes * 60 seconds/minute * 1000 milliseconds/second

        List<Order> expiredOrders = adminOrderRepository.findAllByStatusAndCreatedAtBefore(OrderStatus.PENDING, thirtyMinutesAgo);

        if (!expiredOrders.isEmpty()) {
            System.out.println("========================== AUTO UPDATE ORDER =======================");
            List<UUID> expiredOrderIds = expiredOrders.stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());

            adminOrderHistoryRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderDetailRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderRepository.deleteAllInBatch(expiredOrders);
        }
    }

}
