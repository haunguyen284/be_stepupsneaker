package com.ndt.be_stepupsneaker.core.client.dto.request.review;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientReviewRequest extends PageableRequest {

    private String id;

    private String productDetail;

    private String comment;

    private double rating;

    private String urlImage;

    //filter
    private String product;

}
