package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.repository.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminOrderRepository extends OrderRepository {

    //    @Query("""
//        SELECT o FROM Order o
//        LEFT JOIN o.employee e
//        LEFT JOIN o.customer c
//        LEFT JOIN o.address a
//        LEFT JOIN o.voucher v
//        WHERE o.deleted = false
//        AND COALESCE(:#{#request.q}, '') = '' OR
//            (CONCAT(e.fullName, ' ', e.email, ' ', e.phoneNumber) ILIKE CONCAT('%', COALESCE(:#{#request.q}, ''), '%') OR
//            CONCAT(o.fullName, ' ', c.email, ' ', o.phoneNumber) ILIKE CONCAT('%', COALESCE(:#{#request.q}, ''), '%') OR
//            CONCAT(a.provinceName, ' ', a.districtName, ' ', a.wardName, ' ', a.more) ILIKE CONCAT('%', COALESCE(:#{#request.q}, ''), '%') OR
//            CONCAT(v.code, ' ', v.name) ILIKE CONCAT('%', COALESCE(:#{#request.q}, ''), '%'))
//        AND (:status IS NULL OR o.status = :status)
//        AND (:#{#request.priceMin} IS NULL OR :#{#request.priceMax} IS NULL OR o.totalMoney BETWEEN :#{#request.priceMin} AND :#{#request.priceMax})
//        AND (:#{#request.startDate} IS NULL OR :#{#request.endDate} IS NULL OR o.createdAt BETWEEN :#{#request.startDate} AND :#{#request.endDate})
//
//        """)
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
    o.deleted=false 
    """)
    Page<Order> findAllOrder(
            @Param("request") AdminOrderRequest request,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus status, Long cutoffTime);

    Integer countAllByStatus(OrderStatus status);

}
