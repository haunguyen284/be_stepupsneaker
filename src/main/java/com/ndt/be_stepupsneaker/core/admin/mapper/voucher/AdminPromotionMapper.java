package com.ndt.be_stepupsneaker.core.admin.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionResponse;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminPromotionMapper {

    AdminPromotionMapper INSTANCE = Mappers.getMapper( AdminPromotionMapper.class );

    AdminPromotionResponse promotionToAdminPromotionResponse(Promotion promotion);

    Promotion adminPromotionRequestToPromotion(AdminPromotionRequest promotionRequest);
}
