package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository(OrderHistoryRepository.NAME)
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {
    public static final String NAME = "BaseOrderHistoryRepository";

    Optional<OrderHistory> findByOrder_IdAndActionStatus(String order_id, OrderStatus actionStatus);
}
