package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.order.OrderDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOrderDetailRepository extends OrderDetailRepository {

    @Query("""
    SELECT od FROM OrderDetail od
    LEFT JOIN od.productDetail pd
    WHERE (
    (:#{#request.order} IS NULL OR od.order.id = :#{#request.order}) 
    AND 
    (:#{#request.product} IS NULL OR pd.product.id = :#{#request.product}) 
    AND 
    (:#{#request.brand} IS NULL OR pd.brand.id = :#{#request.brand}) 
    AND 
    (:#{#request.color} IS NULL OR pd.color.id = :#{#request.color}) 
    AND 
    (:#{#request.material} IS NULL OR pd.material.id = :#{#request.material}) 
    AND 
    (:#{#request.size} IS NULL OR pd.size.id = :#{#request.size}) 
    AND 
    (:#{#request.sole} IS NULL OR pd.sole.id = :#{#request.sole}) 
    AND 
    (:#{#request.style} IS NULL OR pd.style.id = :#{#request.style}) 
    AND 
    (:#{#request.tradeMark} IS NULL OR pd.tradeMark.id = :#{#request.tradeMark}) 
    AND 
    ((:status IS NULL) OR (od.status = :status)) 
    AND
    od.deleted=false 
    ) 
    """)
    Page<OrderDetail> findAllOrderDetail(@Param("request")AdminOrderDetailRequest request, @Param("status") OrderStatus status, Pageable pageable);

}
