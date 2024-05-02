package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminStyleRequest;
import com.ndt.be_stepupsneaker.entity.product.Style;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.StyleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminStyleRepository extends StyleRepository {
    @Query("""
    SELECT x FROM Style x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%')) 
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    """)
    Page<Style> findAllStyle(@Param("request") AdminStyleRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    @Query("""
    SELECT x FROM Style x WHERE x.name = :name AND x.deleted=false
    """)
    Optional<Style> findByName(String name);

    @Query("""
    SELECT x FROM Style x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Style y WHERE y.id != :id)) AND x.deleted=false
    """)
    Optional<Style> findByName(@Param("id") String id, @Param("name") String name);


}