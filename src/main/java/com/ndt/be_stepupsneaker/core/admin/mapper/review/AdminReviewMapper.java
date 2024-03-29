package com.ndt.be_stepupsneaker.core.admin.mapper.review;

import com.ndt.be_stepupsneaker.core.admin.dto.request.review.AdminReviewRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.review.AdminReviewResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.entity.review.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminReviewMapper {

    AdminReviewMapper INSTANCE = Mappers.getMapper( AdminReviewMapper.class );

    AdminReviewResponse reviewToAdminReviewResponse(Review  review);

    @Mapping(target = "productDetail.id", source = "productDetail")
//    @Mapping(target = "customer.id", source = "customer")
    Review adminReviewRequestToReview(AdminReviewRequest reviewRequest);
}
