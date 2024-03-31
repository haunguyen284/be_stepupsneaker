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
    SELECT x, 
    COALESCE(SUM(od.quantity), 0) AS saleCount, 
    (SELECT MIN(pd.price) FROM ProductDetail  pd WHERE pd.product = x) as price, 
    (SELECT COALESCE(SUM(pd.quantity), 0) FROM ProductDetail pd WHERE pd.product = x) AS quantity,
    (SELECT COALESCE(AVG (r.rating), 0) FROM Review r WHERE r.productDetail.product = x) AS averageRating
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
    (:#{#request.tradeMarks} IS NULL OR :#{#request.tradeMarks == null ? true : #request.tradeMarks.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.tradeMark.id IN :#{#request.tradeMarks} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.styles} IS NULL OR :#{#request.styles == null ? true : #request.styles.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.style.id IN :#{#request.styles} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.sizes} IS NULL OR :#{#request.sizes == null ? true : #request.sizes.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.size.id IN :#{#request.sizes} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.materials} IS NULL OR :#{#request.materials == null ? true : #request.materials.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.material.id IN :#{#request.materials} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.colors} IS NULL OR :#{#request.colors == null ? true : #request.colors.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.color.id IN :#{#request.colors} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.brands} IS NULL OR :#{#request.brands == null ? true : #request.brands.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.brand.id IN :#{#request.brands} GROUP BY pd.product.id)) 
    AND 
    (:#{#request.soles} IS NULL OR :#{#request.soles == null ? true : #request.soles.empty} = true OR x.id IN (SELECT pd.product.id FROM ProductDetail pd WHERE pd.sole.id IN :#{#request.soles} GROUP BY pd.product.id)) 
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