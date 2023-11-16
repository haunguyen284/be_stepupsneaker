package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.product.ProductDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Transactional
public interface AdminProductDetailRepository extends ProductDetailRepository {
    @Query("""
    SELECT x FROM ProductDetail x 
    WHERE (
    (:#{#request.product} IS NULL OR x.product.id = :#{#request.product}) 
    AND 
    (:#{#request.brand} IS NULL OR x.brand.id = :#{#request.brand}) 
    AND 
    (:#{#request.color} IS NULL OR x.color.id = :#{#request.color}) 
    AND 
    (:#{#request.material} IS NULL OR x.material.id = :#{#request.material}) 
    AND 
    (:#{#request.size} IS NULL OR x.size.id = :#{#request.size}) 
    AND 
    (:#{#request.sole} IS NULL OR x.sole.id = :#{#request.sole}) 
    AND 
    (:#{#request.style} IS NULL OR x.style.id = :#{#request.style}) 
    AND 
    (:#{#request.tradeMark} IS NULL OR x.tradeMark.id = :#{#request.tradeMark}) 
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
    AND 
    (
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.product.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.brand.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.color.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.material.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.size.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.sole.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.style.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
     OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.tradeMark.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
    ) 
    AND 
    (:#{#request.promotion} IS NULL OR x.id IN (SELECT y.productDetail.id FROM PromotionProductDetail y WHERE y.promotion.id = :#{#request.promotion})) 
    AND 
    (CAST(:#{#request.noPromotion} as java.util.UUID) IS NULL OR x.id NOT IN (SELECT y.productDetail.id FROM PromotionProductDetail y WHERE y.promotion.id = CAST(:#{#request.noPromotion} as java.util.UUID))) 
    AND 
    x.deleted=false 
    ) 
    """)
    Page<ProductDetail> findAllProductDetail(@Param("request") AdminProductDetailRequest request, @Param("status") ProductStatus status, Pageable pageable);


    @Query("""
    SELECT x FROM ProductDetail x WHERE (
    x.product.id = :#{#request.product} 
    AND 
    x.brand.id = :#{#request.brand} 
    AND 
    x.color.id = :#{#request.color} 
    AND 
    x.material.id = :#{#request.material} 
    AND 
    x.size.id = :#{#request.size} 
    AND 
    x.sole.id = :#{#request.sole} 
    AND 
    x.style.id = :#{#request.style} 
    AND 
    x.tradeMark.id = :#{#request.tradeMark}
    )
    """)
    Optional<ProductDetail> findByProductProperties(@Param("request") AdminProductDetailRequest request);
}