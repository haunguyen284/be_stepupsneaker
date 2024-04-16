package com.ndt.be_stepupsneaker.core.client.dto.response.review;

import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientReviewResponse {

    private String id;

    private ClientProductDetailResponse productDetail;

    private ClientCustomerResponse customer;

    private String comment;

    private double rating;

    private String urlImage;

    private ReviewStatus status;

    private Long createdAt;

    private Long updatedAt;

}
