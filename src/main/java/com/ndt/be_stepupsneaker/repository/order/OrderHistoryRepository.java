package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(OrderHistoryRepository.NAME)
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, UUID> {
    public static final String NAME = "BaseOrderHistoryRepository";
}
