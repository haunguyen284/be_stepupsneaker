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
            WHERE (
            ((:status IS NULL) OR (rf.status = :status)) 
            AND
            rf.deleted=false 
            ) 
            """)
    Page<ReturnForm> findAllReturnForm(@Param("status") ReturnFormStatus status, Pageable pageable);

    @Query("""
    SELECT rf FROM ReturnForm rf WHERE rf.order.id = :orderId
    """)
    Optional<ReturnForm> findByOrderId(@Param("orderId") String orderId);
}
