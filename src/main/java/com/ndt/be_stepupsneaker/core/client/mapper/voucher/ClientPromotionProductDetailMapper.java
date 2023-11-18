package com.ndt.be_stepupsneaker.core.client.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPromotionProductDetailMapper {
    ClientPromotionProductDetailMapper INSTANCE = Mappers.getMapper( ClientPromotionProductDetailMapper.class );

    ClientPromotionProductDetailResponse promotionProductDetailToClientPromotionProductDetailResponse(PromotionProductDetail promotionProductDetail);

    @Mapping(target = "productDetail.id", source = "productDetail")
    @Mapping(target = "promotion.id", source = "promotion")
    PromotionProductDetail clientPromotionProductDetailRequestToPromotionProductDetail(ClientPromotionProductDetailRequest productDetailRequest);
}
