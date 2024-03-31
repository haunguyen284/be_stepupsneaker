package com.ndt.be_stepupsneaker.core.admin.dto.response.review;

import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReviewStatus;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReviewResponse {

    private String id;

    private AdminProductDetailResponse productDetail;

    private AdminCustomerResponse customer;

    private String comment;

    private int rating;

    private String urlImage;

    private ReviewStatus status;

    private Long createdAt;


}
