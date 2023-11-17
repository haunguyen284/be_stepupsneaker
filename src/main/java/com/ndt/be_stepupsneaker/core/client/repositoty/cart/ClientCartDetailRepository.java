package com.ndt.be_stepupsneaker.core.client.repositoty.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.repository.cart.CartDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface ClientCartDetailRepository extends CartDetailRepository {
    @Query("""
            SELECT x FROM CartDetail x 
            WHERE (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' 
            OR x.productDetail.product.name ILIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.product.code ILIKE  CONCAT('%', :#{#request.q}, '%') 
            OR x.productDetail.product.description ILIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.brand.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.style.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.tradeMark.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.color.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.sole.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.size.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.productDetail.material.name ILIKE CONCAT('%', :#{#request.q}, '%'))
            AND 
            (:#{#request.customer} IS NULL OR x.cart.customer = :#{#request.customer}) 
            AND
            x.deleted = FALSE
            """)
    Page<CartDetail> findAllCartDetail(@Param("request") ClientCartDetailRequest request, Pageable pageable);


    CartDetail findByProductDetailAndCart(ProductDetail productDetail, Cart cart);

    @Modifying
    @Transactional
    void deleteAllByIdIn(List<UUID> cartDetailIds);

}
