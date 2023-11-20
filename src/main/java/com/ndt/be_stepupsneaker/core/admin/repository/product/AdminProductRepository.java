package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
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
import java.util.UUID;



@Repository
@Transactional
public interface AdminProductRepository extends ProductRepository {
    @Query("""
    SELECT x FROM Product x 
    WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%')) 
    AND
    (:#{#request.code} IS NULL OR :#{#request.code} ILIKE '' OR x.code ILIKE  CONCAT('%', :#{#request.code}, '%')) 
    AND 
    (
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.code ILIKE  CONCAT('%', :#{#request.q}, '%')) OR 
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%'))
    )
    AND 
    ((:status IS NULL) OR (x.status = :status)) 
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
    (:#{#request.minQuantity} IS NULL OR :#{#request.minQuantity} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd GROUP BY pd.product.id HAVING SUM(pd.quantity) >= CAST(:#{#request.minQuantity} AS int)))
    AND 
    (:#{#request.maxQuantity} IS NULL OR :#{#request.maxQuantity} ILIKE '' OR x.id IN (SELECT pd.product.id FROM ProductDetail pd GROUP BY pd.product.id HAVING SUM(pd.quantity) <= CAST(:#{#request.maxQuantity} AS int))) 
    AND 
    x.deleted=false 
    """)
    Page<Product> findAllProduct(@Param("request") AdminProductRequest request, @Param("status") ProductStatus status, Pageable pageable);

    Optional<Product> findByName(String name);

    @Query("""
    SELECT x FROM Product x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Product y WHERE y.id != :id))
    """)
    Optional<Product> findByName(@Param("id") String id, @Param("name") String name);

    @Query("""
    SELECT x FROM Product x WHERE x.code = :code AND :code IN (SELECT y.code FROM Product y WHERE y.id != :id)
    """)
    Optional<Product> findByCode(@Param("id")String id, @Param("code") String code);
    
    Optional<Product> findByCode(String code);

}