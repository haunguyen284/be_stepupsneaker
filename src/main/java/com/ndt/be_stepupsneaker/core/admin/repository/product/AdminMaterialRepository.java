package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminMaterialRequest;
import com.ndt.be_stepupsneaker.entity.product.Material;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.MaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AdminMaterialRepository extends MaterialRepository {
    @Query("""
    SELECT x FROM Material x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%')) 
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    """)
    Page<Material> findAllMaterial(@Param("request") AdminMaterialRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    Optional<Material> findByName(String name);

    @Query("""
    SELECT x FROM Material x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Material y WHERE y.id != :id))
    """)
    Optional<Material> findByName(@Param("id")UUID id, @Param("name") String name);


}
