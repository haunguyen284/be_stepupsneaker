package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(OrderRepository.NAME)
public interface OrderRepository extends JpaRepository<Order, String> {
    public static final String NAME = "BaseOrderRepository";
}
