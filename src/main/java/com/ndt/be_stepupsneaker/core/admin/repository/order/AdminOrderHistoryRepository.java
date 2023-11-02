package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.repository.order.OrderHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
