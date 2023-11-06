package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.repository.order.OrderHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminOrderHistoryRepository extends OrderHistoryRepository {

    @Query("""
    SELECT oh FROM OrderHistory oh
    WHERE (
    (:#{#request.order} IS NULL OR oh.order.id = :#{#request.order}) 
    AND
    oh.deleted=false 
    ) 
    """)
    Page<OrderHistory> findAllOrderHistory(@Param("request")AdminOrderHistoryRequest request, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderHistory x WHERE x.order.id IN :orderIds")
    void deleteAllByOrder(List<UUID> orderIds);

    Optional<OrderHistory> findByOrder_IdAndActionStatus(UUID order_id, OrderStatus actionStatus);
}
