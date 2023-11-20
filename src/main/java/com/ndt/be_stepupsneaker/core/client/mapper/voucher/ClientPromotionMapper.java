package com.ndt.be_stepupsneaker.core.client.mapper.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientPromotionRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientPromotionResponse;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPromotionMapper {

    ClientPromotionMapper INSTANCE = Mappers.getMapper( ClientPromotionMapper.class );

    ClientPromotionResponse promotionToClientPromotionResponse(Promotion promotion);

    Promotion clientPromotionRequestToPromotion(ClientPromotionRequest promotionRequest);
}
