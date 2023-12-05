package com.ndt.be_stepupsneaker.core.client.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.entity.product.Product;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Transactional
public interface ClientProductRepository extends ProductRepository {
    @Query("""
    SELECT x, COALESCE(SUM(od.quantity), 0) AS saleCount 
    FROM Product x 
    LEFT JOIN x.productDetails pd 
    LEFT JOIN OrderDetail od ON pd.id = od.productDetail.id 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND
    (:#{#request.code} IS NULL OR :#{#request.code} ILIKE '' OR x.code ILIKE  CONCAT('%', :#{#request.code}, '%')) 
    AND 
    (
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.code ILIKE  CONCAT('%', :#{#request.q}, '%')) OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
    ) 
    AND 
    (:#{#request.tradeMark} IS NULL OR :#{#request.tradeMark} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.tradeMark.id = :#{#request.tradeMark} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.style} IS NULL OR :#{#request.style} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.style.id = :#{#request.style} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.size} IS NULL OR :#{#request.size} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.size.id = :#{#request.size} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.product} IS NULL OR :#{#request.product} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.product.id = :#{#request.product} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.material} IS NULL OR :#{#request.material} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.material.id = :#{#request.material} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.color} IS NULL OR :#{#request.color} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.color.id = :#{#request.color} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.brand} IS NULL OR :#{#request.brand} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.brand.id = :#{#request.brand} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.sole} IS NULL OR :#{#request.sole} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.sole.id = :#{#request.sole} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.start} IS NULL OR :#{#request.start} ILIKE '' OR x.createdAt >= CAST(:#{#request.start} as long)) 
    AND 
    (:#{#request.end} IS NULL OR :#{#request.end} ILIKE '' OR x.createdAt <= CAST(:#{#request.end} as long)) 
    AND 
    (:#{#request.minQuantity} IS NULL OR :#{#request.minQuantity} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd GROUP BY pd.product.id HAVING SUM(pd.quantity) >= CAST(:#{#request.minQuantity} AS int)))
    AND 
    (:#{#request.maxQuantity} IS NULL OR :#{#request.maxQuantity} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd GROUP BY pd.product.id HAVING SUM(pd.quantity) <= CAST(:#{#request.maxQuantity} AS int))) 
    AND 
    x.status=0 
    AND 
    x.deleted=false 
    GROUP BY x.id
    """)
    Page<Object[]> findAllProduct(@Param("request") ClientProductRequest request, Pageable pageable);



}