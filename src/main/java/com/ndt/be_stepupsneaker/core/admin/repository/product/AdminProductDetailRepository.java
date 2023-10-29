package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.product.ProductDetailRepository;
import com.ndt.be_stepupsneaker.repository.product.ProductDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AdminProductDetailRepository extends ProductDetailRepository {
    @Query("""
    SELECT x FROM ProductDetail x 
    WHERE (
    (:#{#request.tradeMark} IS NULL OR x.tradeMark = :#{#request.tradeMark})
    AND
    (:#{#request.style} IS NULL OR x.style = :#{#request.style})
    AND
    (:#{#request.size} IS NULL OR x.size = :#{#request.size})
    AND
    (:#{#request.product} IS NULL OR x.product.id = :#{#request.product})
    AND
    (:#{#request.material} IS NULL OR x.material = :#{#request.material})
    AND
    (:#{#request.color} IS NULL OR x.color = :#{#request.color})
    AND
    (:#{#request.brand} IS NULL OR x.brand = :#{#request.brand})
    AND
    (:#{#request.sole} IS NULL OR x.sole = :#{#request.sole}) 
    AND 
    (:#{#request.priceMin} IS NULL OR :#{#request.priceMax} IS NULL OR x.price BETWEEN :#{#request.priceMin} AND :#{#request.priceMax}) 
    AND
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    ) 
    """)
    Page<ProductDetail> findAllProductDetail(@Param("request") AdminProductDetailRequest request, @Param("status") ProductStatus status, Pageable pageable);


}