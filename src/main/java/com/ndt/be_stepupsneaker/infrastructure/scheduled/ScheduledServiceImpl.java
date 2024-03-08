package com.ndt.be_stepupsneaker.infrastructure.scheduled;

import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.repository.cart.ClientCartDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.util.ConvertUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduledServiceImpl implements ScheduledService {
    private final AdminVoucherRepository adminVoucherRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final AdminPromotionRepository adminPromotionRepository;
    private final ClientCartDetailRepository clientCartDetailRepository;
    private final ClientAddressRepository clientAddressRepository;
    private final ClientPaymentRepository clientPaymentRepository;
    private final ClientVoucherHistoryRepository clientVoucherHistoryRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;


    @Override
    public void updateVoucherStatusAutomatically() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Long currentLongTime = ConvertUtil.convertLocalDateTimeToLong(currentDateTime);
        adminVoucherRepository.updateStatusAutomatically(currentLongTime);
        adminPromotionRepository.updateStatusAutomatically(currentLongTime);
    }

    @Override
    public void deleteCartDetailsByDate() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        Long thirtyDaysAgoTimestamp = ConvertUtil.convertLocalDateTimeToLong(thirtyDaysAgo);
        clientCartDetailRepository.deleteByUpdatedAtBefore(thirtyDaysAgoTimestamp);
    }


    @Transactional
    @Override
    public void updateOrderAutomatically() {
        long currentMillis = Instant.now().toEpochMilli();
        // Calculate the time 30 minutes ago in milliseconds
        long thirtyMinutesAgo = currentMillis - (30 * 60 * 1000); // 30 minutes * 60 seconds/minute * 1000 milliseconds/second
        List<ProductDetail> productDetails = new ArrayList<>();
        List<Order> expiredOrders = adminOrderRepository.findAllByStatusAndCreatedAtBefore(OrderStatus.PENDING, thirtyMinutesAgo);

        if (!expiredOrders.isEmpty()) {
            List<Voucher> vouchers = new ArrayList<>();
            List<String> expiredOrderIds = expiredOrders.stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());
            for (Order order : expiredOrders) {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                for (OrderDetail orderDetail : orderDetails) {
                    ProductDetail productDetail = orderDetail.getProductDetail();
                    productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
                    productDetails.add(productDetail);
                }
                if (order.getVoucher() != null) {
                    Voucher voucher = order.getVoucher();
                    voucher.setQuantity(order.getVoucher().getQuantity() + 1);
                    vouchers.add(order.getVoucher());
                }
            }
            adminVoucherRepository.saveAll(vouchers);
            clientVoucherHistoryRepository.deleteVoucherHistoryByOrder(expiredOrderIds);
            adminProductDetailRepository.saveAll(productDetails);
            adminOrderDetailRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderHistoryRepository.deleteAllByOrder(expiredOrderIds);
            adminOrderRepository.deleteAllInBatch(expiredOrders);
        }
    }

}
