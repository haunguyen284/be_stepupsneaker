package com.ndt.be_stepupsneaker.core.client.repository.review;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.repository.review.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientReviewRepository extends ReviewRepository {
    @Query("""
            SELECT x FROM Review x
            WHERE  x.deleted = FALSE AND x.status = 1
            AND(x.productDetail.product.id = :#{#request.product})
            """)
    Page<Review> findAllReview(@Param("request") ClientReviewRequest request, Pageable pageable);

    @Query("""
            SELECT x FROM Review x
            WHERE x.deleted = FALSE AND x.customer.id = :customer
            AND(x.productDetail.product.id = :#{#request.product}) AND x.status = 0
            """)
    Page<Review> findAllReviewByCustomerAndStatus(@Param("request") ClientReviewRequest request, Pageable pageable,
                                                  @Param("customer") String customer);

    Optional<Review> findByCustomerAndId(Customer customer, String id);
}
