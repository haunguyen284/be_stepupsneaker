package com.ndt.be_stepupsneaker.core.client.mapper.review;

import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.voucher.ClientCustomerVoucherMapper;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientReviewMapper {

    ClientReviewMapper INSTANCE = Mappers.getMapper( ClientReviewMapper.class );

    ClientReviewResponse reviewToClientReviewResponse(Review  review);

    @Mapping(target = "productDetail.id", source = "productDetail")
    @Mapping(target = "order.id", source = "order")
//    @Mapping(target = "customer.id", source = "customer")
    Review clientReviewRequestToReview(ClientReviewRequest clientReviewRequest);
}
