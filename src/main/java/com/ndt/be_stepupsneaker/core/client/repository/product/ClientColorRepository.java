package com.ndt.be_stepupsneaker.core.client.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientColorRequest;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.ColorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientColorRepository extends ColorRepository {
    @Query("""
    SELECT x FROM Color x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND
    (:#{#request.code} IS NULL OR :#{#request.code} ILIKE '' OR x.code ILIKE  CONCAT('%', :#{#request.code}, '%')) 
    AND 
    (
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.code ILIKE  CONCAT('%', :#{#request.q}, '%')) OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
    )
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    """)
    Page<Color> findAllColor(@Param("request") ClientColorRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);
}
