package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminBrandRepository extends BrandRepository {
    @Query("""
    SELECT x FROM Brand x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%'))
     AND 
     (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%')) 
     AND 
     ((:status IS NULL) OR (x.status = :status)) 
     AND
     x.deleted=false
    """)
    Page<Brand> findAllBrand(@Param("request") AdminBrandRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    @Query("""
    SELECT x FROM Brand x WHERE x.name = :name AND x.deleted=false
    """)
    Optional<Brand> findByName(String name);

    @Query("""
    SELECT x FROM Brand x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Brand y WHERE y.id != :id AND y.deleted=false))
    """)
    Optional<Brand> findByName(@Param("id") String id, @Param("name") String name);
}
