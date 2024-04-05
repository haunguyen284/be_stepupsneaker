package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
import com.ndt.be_stepupsneaker.repository.order.ReturnFormRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminReturnFormRepository extends ReturnFormRepository {
    @Query("""
            SELECT rf FROM ReturnForm rf
            WHERE 
            (
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR rf.code ILIKE  CONCAT('%', :#{#request.q}, '%')) 
            OR 
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR rf.order.customer.email ILIKE  CONCAT('%', :#{#request.q}, '%')) 
            OR 
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR rf.order.phoneNumber ILIKE  CONCAT('%', :#{#request.q}, '%'))
            ) 
            AND 
            ((:#{#request.returnDeliveryStatus} IS NULL) OR (rf.returnDeliveryStatus = :#{#request.returnDeliveryStatus})) 
            AND 
            ((:#{#request.refundStatus} IS NULL) OR (rf.refundStatus = :#{#request.refundStatus})) 
            AND 
            ((:#{#request.paymentType} IS NULL) OR (:#{#request.paymentType} ILIKE '') OR (rf.paymentType = :#{#request.paymentType})) 
            AND 
            rf.deleted=false 
            """)
    Page<ReturnForm> findAllReturnForm(@Param("request") AdminReturnFormRequest request, Pageable pageable);

    @Query("""
    SELECT rf FROM ReturnForm rf WHERE rf.order.id = :orderId
    """)
    Optional<ReturnForm> findByOrderId(@Param("orderId") String orderId);
}
