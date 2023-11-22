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
    GROUP BY x.id
    """)
    Page<Object[]> findAllProduct(@Param("request") ClientProductRequest request, Pageable pageable);



}