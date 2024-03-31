package com.ndt.be_stepupsneaker.core.admin.dto.request.review;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReviewRequest extends PageableRequest {

    private String id;

    private String productDetail;

    private String comment;

    private double rating;

    private String urlImage;

    private ReviewStatus status;

    //filter
    private String product;

}
