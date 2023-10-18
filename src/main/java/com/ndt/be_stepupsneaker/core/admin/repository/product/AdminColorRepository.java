package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.product.ColorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminColorRepository extends ColorRepository {
    @Query("""
    SELECT x FROM Color x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} LIKE '' OR x.name LIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND
    (:#{#request.code} IS NULL OR :#{#request.code} LIKE '' OR x.code LIKE  CONCAT('%', :#{#request.code}, '%')) 
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    """)
    Page<Color> findAllColor(@Param("request") AdminColorRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    Optional<Color> findByName(String name);
    Optional<Color> findByCode(String code);


    @Query("""
    SELECT x FROM Color x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Color y WHERE y.id != :id))
    """)
    Optional<Color> findByName(@Param("id")UUID id, @Param("name") String name);

    @Query("""
    SELECT x FROM Color x WHERE x.code = :code AND :code IN (SELECT y.code FROM Color y WHERE y.id != :id)
    """)
    Optional<Color> findByCode(@Param("id")UUID id, @Param("code") String code);

}
