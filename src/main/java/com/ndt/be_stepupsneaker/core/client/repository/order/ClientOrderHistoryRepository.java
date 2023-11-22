package com.ndt.be_stepupsneaker.core.client.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderHistoryRequest;
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

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientOrderHistoryRepository extends OrderHistoryRepository {

    @Query("""
    SELECT oh FROM OrderHistory oh
    WHERE oh.order.id = :#{#request.order} 
    AND
    oh.deleted=false 
    """)
    Page<OrderHistory> findAllOrderHistory(@Param("request")ClientOrderHistoryRequest request, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderHistory x WHERE x.order.id IN :orderIds")
    void deleteAllByOrder(List<String> orderIds);

    Optional<OrderHistory> findByOrder_IdAndActionStatus(String order_id, OrderStatus actionStatus);
}
