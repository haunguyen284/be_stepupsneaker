package com.ndt.be_stepupsneaker.core.admin.mapper.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartResponse;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminCartDetailMapper {

    AdminCartDetailMapper INSTANCE = Mappers.getMapper(AdminCartDetailMapper.class);

    AdminCartDetailResponse cartDetailToAdminCartDetailResponse(CartDetail cartDetail);

    @Mapping(target = "productDetail.id",source = "productDetail")
    @Mapping(target = "cart.id",source = "cart")
    CartDetail adminCartDetailRequestToCartDetail(AdminCartDetailRequest cartDetailRequest);

}
