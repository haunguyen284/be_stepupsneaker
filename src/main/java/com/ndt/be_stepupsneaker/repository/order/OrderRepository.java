package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository(OrderRepository.NAME)
public interface OrderRepository extends JpaRepository<Order, String>, RevisionRepository<Order,String, Integer> {
    public static final String NAME = "BaseOrderRepository";
    Optional<Order> findByCode(String code);
}
