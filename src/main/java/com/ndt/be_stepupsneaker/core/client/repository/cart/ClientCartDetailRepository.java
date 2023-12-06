package com.ndt.be_stepupsneaker.core.client.repository.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.repository.cart.CartDetailRepository;
import com.ndt.be_stepupsneaker.repository.cart.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            (x.cart = :cart) 
            AND
            x.deleted = FALSE
            """)
    Page<CartDetail> findAllCartDetail(@Param("request") ClientCartDetailRequest request,
                                       @Param("cart") Cart cart, Pageable pageable);

    CartDetail findByProductDetailAndCart(ProductDetail productDetail, Cart cart);

    CartDetail findByIdAndCart(String id, Cart cart);

    @Modifying
    @Transactional
    void deleteAllByIdInAndCart(List<String> cartDetailIds, Cart cart);

    @Modifying
    @Transactional
    void deleteByUpdatedAtBefore(Long date);
}
