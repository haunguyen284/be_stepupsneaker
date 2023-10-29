package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.product.ProductDetailRepository;
import com.ndt.be_stepupsneaker.repository.product.ProductDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@Transactional
public interface AdminProductDetailRepository extends ProductDetailRepository {
    @Query("""
    SELECT x FROM ProductDetail x 
    WHERE (
    (:#{#request.tradeMark} IS NULL OR :#{#request.tradeMark.toString()} LIKE '' OR x.tradeMark.id = :#{#request.tradeMark})
    AND
    (:#{#request.style} IS NULL OR :#{#request.style.toString()} LIKE '' OR x.style.id = :#{#request.style})
    AND
    (:#{#request.size} IS NULL OR :#{#request.size.toString()} LIKE '' OR x.size.id = :#{#request.size})
    AND
    (:#{#request.product} IS NULL OR :#{#request.product.toString()} LIKE '' OR x.product.id = :#{#request.product})
    AND
    (:#{#request.material} IS NULL OR :#{#request.material.toString()} LIKE '' OR x.material.id = :#{#request.material})
    AND
    (:#{#request.color} IS NULL OR :#{#request.color.toString()} LIKE '' OR x.color.id = :#{#request.color})
    AND
    (:#{#request.brand} IS NULL OR :#{#request.brand.toString()} LIKE '' OR x.brand.id = :#{#request.brand})
    AND
    (:#{#request.sole} IS NULL OR :#{#request.sole.toString()} LIKE '' OR x.sole.id = :#{#request.sole}) 
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND
    x.deleted=false 
    ) 
    """)
    Page<ProductDetail> findAllProductDetail(@Param("request") AdminProductDetailRequest request, @Param("status") ProductStatus status, Pageable pageable);


}