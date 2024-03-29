package com.ndt.be_stepupsneaker.core.admin.repository.review;

import com.ndt.be_stepupsneaker.core.admin.dto.request.review.AdminReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
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
public interface AdminReviewRepository extends ReviewRepository {
    @Query("""
            SELECT x FROM Review x 
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
            OR x.productDetail.material.name ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.comment ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.customer.fullName ILIKE CONCAT('%', :#{#request.q}, '%')
            OR x.customer.email ILIKE CONCAT('%', :#{#request.q}, '%'))
            AND x.status != 2 AND x.deleted = FALSE
            """)
    Page<Review> findAllReview(@Param("request") AdminReviewRequest request, Pageable pageable);

}
