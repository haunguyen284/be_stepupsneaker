package com.ndt.be_stepupsneaker.core.client.dto.request.review;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientReviewRequest extends PageableRequest {

    private String id;

    private String productDetail;

    private String comment;

    private int rating;

    private String urlImage;

    //filter
    private String product;

}
