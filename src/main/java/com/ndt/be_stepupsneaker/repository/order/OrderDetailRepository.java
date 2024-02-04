package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository(OrderDetailRepository.NAME)
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    public static final String NAME = "BaseOrderDetailRepository";


    @Query("SELECT od FROM OrderDetail od " +
            "WHERE od.order = :order " +
            "AND od.productDetail NOT IN " +
            "(SELECT od2.productDetail FROM OrderDetail od2 WHERE od2.order = :order " +
            "GROUP BY od2.productDetail HAVING COUNT(od2.productDetail) > 1)")
    List<OrderDetail> findDistinctByOrder(Order order);


    List<OrderDetail> findAllByOrder(Order order);
}
