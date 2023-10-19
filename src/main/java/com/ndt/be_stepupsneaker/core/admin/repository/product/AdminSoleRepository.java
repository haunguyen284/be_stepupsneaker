package com.ndt.be_stepupsneaker.core.admin.repository.product;


import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSoleRequest;
import com.ndt.be_stepupsneaker.entity.product.Sole;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.SoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AdminSoleRepository extends SoleRepository {
    @Query("""
    SELECT x FROM Sole x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} LIKE '' OR x.name LIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    """)
    Page<Sole> findAllSole(@Param("request") AdminSoleRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    Optional<Sole> findByName(String name);

    @Query("""
    SELECT x FROM Sole x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Sole y WHERE y.id != :id))
    """)
    Optional<Sole> findByName(@Param("id") UUID id, @Param("name") String name);


}