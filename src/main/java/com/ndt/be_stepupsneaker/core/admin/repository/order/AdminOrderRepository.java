package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.repository.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminOrderRepository extends OrderRepository {

    @Query("""
            SELECT o FROM Order o 
            WHERE 
            (
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR o.code ILIKE  CONCAT('%', :#{#request.q}, '%')) OR 
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR o.fullName ILIKE  CONCAT('%', :#{#request.q}, '%'))
            )
            AND 
            ((:status IS NULL) OR (o.status = :status)) 
            AND 
            ((:type IS NULL) OR (o.type = :type)) 
            AND 
            ((:#{#request.customer} IS NULL) OR (o.customer.id = :#{#request.customer})) 
            AND
            (:#{#request.priceMin} IS NULL OR :#{#request.priceMin} = 0 OR o.totalMoney >= :#{#request.priceMin})
            AND
            (:#{#request.priceMax} IS NULL OR :#{#request.priceMax} = 0 OR o.totalMoney <= :#{#request.priceMax})
            AND
            o.deleted=false 
            """)
    Page<Order> findAllOrder(
            @Param("request") AdminOrderRequest request,
            @Param("status") OrderStatus status,
            @Param("type") OrderType type,
            Pageable pageable
    );

    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus status, Long cutoffTime);

    Integer countAllByStatus(OrderStatus status);

}
