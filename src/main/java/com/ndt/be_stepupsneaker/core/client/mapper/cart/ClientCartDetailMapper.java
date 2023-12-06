package com.ndt.be_stepupsneaker.core.client.mapper.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartDetailResponse;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientCartDetailMapper {
    ClientCartDetailMapper INSTANCE = Mappers.getMapper(ClientCartDetailMapper.class);

    ClientCartDetailResponse cartDetailToClientCartDetailResponse(CartDetail cartDetail);

    @Mapping(target = "productDetail.id",source = "productDetail")
    CartDetail clientCartDetailRequestToCartDetail(ClientCartDetailRequest cartDetailRequest);
}
