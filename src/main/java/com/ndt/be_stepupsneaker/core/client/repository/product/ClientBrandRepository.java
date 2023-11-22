package com.ndt.be_stepupsneaker.core.client.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientBrandRequest;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientBrandRepository extends BrandRepository {
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
    Page<Brand> findAllBrand(@Param("request") ClientBrandRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);
}
