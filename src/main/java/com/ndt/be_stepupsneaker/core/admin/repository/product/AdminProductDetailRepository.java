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

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface AdminProductDetailRepository extends ProductDetailRepository {
    @Query("""
    SELECT x FROM ProductDetail x 
    WHERE (
    (:#{#request.products} IS NULL OR :#{#request.products == null ? true : #request.products.empty} = true OR x.product.id IN :#{#request.products}) 
    AND 
    (:#{#request.brands} IS NULL OR :#{#request.brands == null ? true : #request.brands.empty} = true OR x.brand.id IN :#{#request.brands}) 
    AND 
    (:#{#request.colors} IS NULL OR :#{#request.colors == null ? true : #request.colors.empty} = true OR x.color.id IN :#{#request.colors}) 
    AND 
    (:#{#request.materials} IS NULL OR :#{#request.materials == null ? true : #request.materials.empty} = true OR x.material.id IN :#{#request.materials}) 
    AND 
    (:#{#request.sizes} IS NULL OR :#{#request.sizes == null ? true : #request.sizes.empty} = true OR x.size.id IN :#{#request.sizes}) 
    AND 
    (:#{#request.soles} IS NULL OR :#{#request.soles == null ? true : #request.soles.empty} = true OR x.sole.id IN :#{#request.soles}) 
    AND 
    (:#{#request.styles} IS NULL OR :#{#request.styles == null ? true : #request.styles.empty} = true OR x.style.id IN :#{#request.styles}) 
    AND 
    (:#{#request.tradeMarks} IS NULL OR :#{#request.tradeMarks == null ? true : #request.tradeMarks.empty} = true OR x.tradeMark.id IN :#{#request.tradeMarks}) 
    AND 
    (
    (:#{#request.promotion} IS NULL OR :#{#request.isInPromotion} = 1 AND x.id IN (SELECT y.productDetail.id FROM PromotionProductDetail y WHERE y.promotion.id = :#{#request.promotion})) 
    OR 
    (:#{#request.promotion} IS NULL OR :#{#request.isInPromotion} = 0 AND x.id NOT IN (SELECT y.productDetail.id FROM PromotionProductDetail y WHERE y.promotion.id = :#{#request.promotion}))
    ) 
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

    @Query("""
    SELECT pd FROM OrderDetail od 
    JOIN od.productDetail pd 
    JOIN od.order o 
    WHERE o.status = 4 
    AND o.createdAt BETWEEN :fromDate AND :toDate 
    GROUP BY pd.id 
    ORDER BY SUM(od.quantity) DESC 
    LIMIT 5
    """)
    List<ProductDetail> findProductDetailTrending(@Param("fromDate") Long fromDate, @Param("toDate") Long toDate);
}