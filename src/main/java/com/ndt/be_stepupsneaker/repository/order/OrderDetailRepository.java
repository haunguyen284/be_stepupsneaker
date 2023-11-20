package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(OrderDetailRepository.NAME)
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    public static final String NAME = "BaseOrderDetailRepository";
}
