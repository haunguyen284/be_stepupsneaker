package com.ndt.be_stepupsneaker.core.admin.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminPromotionProductDetailMapper {
    AdminPromotionProductDetailMapper INSTANCE = Mappers.getMapper( AdminPromotionProductDetailMapper.class );

    AdminPromotionProductDetailResponse promotionProductDetailToAdminPromotionProductDetailResponse(PromotionProductDetail promotionProductDetail);

    @Mapping(target = "productDetail.id", source = "productDetail")
    @Mapping(target = "promotion.id", source = "promotion")
    PromotionProductDetail adminPromotionProductDetailRequestToPromotionProductDetail(AdminPromotionProductDetailRequest productDetailRequest);
}
