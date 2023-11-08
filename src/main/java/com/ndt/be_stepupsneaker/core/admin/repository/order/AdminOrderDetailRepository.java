package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.order.OrderDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminOrderDetailRepository extends OrderDetailRepository {

    @Query("""
    SELECT od FROM OrderDetail od
    WHERE (
    (:#{#request.order} IS NULL OR od.order.id = :#{#request.order}) 
    AND 
    (:#{#request.product} IS NULL OR od.productDetail.product.id = :#{#request.product}) 
    AND 
    (:#{#request.brand} IS NULL OR od.productDetail.brand.id = :#{#request.brand}) 
    AND 
    (:#{#request.color} IS NULL OR od.productDetail.color.id = :#{#request.color}) 
    AND 
    (:#{#request.material} IS NULL OR od.productDetail.material.id = :#{#request.material}) 
    AND 
    (:#{#request.size} IS NULL OR od.productDetail.size.id = :#{#request.size}) 
    AND 
    (:#{#request.sole} IS NULL OR od.productDetail.sole.id = :#{#request.sole}) 
    AND 
    (:#{#request.style} IS NULL OR od.productDetail.style.id = :#{#request.style}) 
    AND 
    (:#{#request.tradeMark} IS NULL OR od.productDetail.tradeMark.id = :#{#request.tradeMark}) 
    AND 
    ((:status IS NULL) OR (od.status = :status)) 
    AND
    od.deleted=false 
    ) 
    """)
    Page<OrderDetail> findAllOrderDetail(@Param("request")AdminOrderDetailRequest request, @Param("status") OrderStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderDetail x WHERE x.order.id IN :orderIds")
    void deleteAllByOrder(List<UUID> orderIds);
}
