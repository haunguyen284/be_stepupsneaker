package com.ndt.be_stepupsneaker.core.client.repository.order;

import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.repository.order.ReturnFormRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientReturnFormRepository extends ReturnFormRepository {
    @Query("""
            SELECT rf FROM ReturnForm rf
            WHERE rf.deleted=false AND rf.order.customer.id = :customerId
            """)
    Page<ReturnForm> findAllReturnForm(@Param("customerId")String customerId, Pageable pageable);

    @Query("""
    SELECT rf FROM ReturnForm rf WHERE rf.id = :id AND rf.order.customer.id = :customerId
    """)
    Optional<ReturnForm> findByEntityById(@Param("id") String id, @Param("customerId") String customerId);

    @Query("""
    SELECT rf FROM ReturnForm rf WHERE rf.code = :code AND rf.deleted=false
    """)
    Optional<ReturnForm> findEntityByCode(@Param("code") String code);
}
