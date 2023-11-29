package com.ndt.be_stepupsneaker.infrastructure.scheduled;

import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.repository.cart.ClientCartDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.util.ConvertTime;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutoScheduledServiceImpl implements AutoScheduledService {
    private final AdminVoucherRepository adminVoucherRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final AdminPromotionRepository adminPromotionRepository;
    private final ClientCartDetailRepository clientCartDetailRepository;
    private final ClientAddressRepository clientAddressRepository;
    private final ClientPaymentRepository clientPaymentRepository;
    private final ClientVoucherHistoryRepository clientVoucherHistoryRepository;


    @Autowired
    public AutoScheduledServiceImpl(
            AdminVoucherRepository adminVoucherRepository,
            AdminOrderRepository adminOrderRepository,
            AdminOrderHistoryRepository adminOrderHistoryRepository,
            AdminOrderDetailRepository adminOrderDetailRepository,
            AdminPromotionRepository adminPromotionRepository,
            ClientCartDetailRepository clientCartDetailRepository,
            ClientAddressRepository clientAddressRepository,
            ClientPaymentRepository clientPaymentRepository,
            ClientVoucherHistoryRepository clientVoucherHistoryRepository) {
        this.adminVoucherRepository = adminVoucherRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.adminOrderHistoryRepository = adminOrderHistoryRepository;
        this.adminOrderDetailRepository = adminOrderDetailRepository;
        this.adminPromotionRepository = adminPromotionRepository;
        this.clientCartDetailRepository = clientCartDetailRepository;
        this.clientAddressRepository = clientAddressRepository;
        this.clientPaymentRepository = clientPaymentRepository;
        this.clientVoucherHistoryRepository = clientVoucherHistoryRepository;
    }

    @Override
    public void updateVoucherStatusAutomatically() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Long currentLongTime = ConvertTime.convertLocalDateTimeToLong(currentDateTime);
        adminVoucherRepository.updateStatusAutomatically(currentLongTime);
    }

    @Override
    public void updatePromotionStatusAutomatically() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Long currentLongTime = ConvertTime.convertLocalDateTimeToLong(currentDateTime);
        adminPromotionRepository.updateStatusAutomatically(currentLongTime);
    }

    @Override
    public void deleteCartDetailsByDate() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Long thirtyDaysAgoTimestamp = ConvertTime.convertLocalDateTimeToLong(thirtyDaysAgo);
        clientCartDetailRepository.deleteByUpdatedAtBefore(thirtyDaysAgoTimestamp);
    }
    @Transactional
    @Override
    public void deleteOrderAutomaticallyByTypeAndStatus() {
        long currentMillis = Instant.now().toEpochMilli();
        long thirtyMinutesAgo = currentMillis - (30 * 60 * 1000);
        List<Order> expiredOrders = adminOrderRepository.findAllByStatusAndCreatedAtBeforeAndType(OrderStatus.PENDING, thirtyMinutesAgo, OrderType.ONLINE);
        if (!expiredOrders.isEmpty()) {
            List<String> expiredOrderIds = expiredOrders.stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());

            clientVoucherHistoryRepository.deleteVoucherHistoryByOrder(expiredOrderIds);
            clientPaymentRepository.deleteAddressByOrder1(expiredOrderIds);
            clientPaymentRepository.deletePaymentByOrder(expiredOrderIds);
            adminOrderHistoryRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderDetailRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderRepository.deleteAllInBatch(expiredOrders);
            clientAddressRepository.deleteAddressByOrder(expiredOrderIds);
        }
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
            List<String> expiredOrderIds = expiredOrders.stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());

            adminOrderHistoryRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderDetailRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderRepository.deleteAllInBatch(expiredOrders);
        }
    }

}
