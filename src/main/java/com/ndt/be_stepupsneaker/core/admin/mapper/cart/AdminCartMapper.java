package com.ndt.be_stepupsneaker.core.admin.mapper.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartResponse;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminCartMapper {
    AdminCartMapper INSTANCE = Mappers.getMapper(AdminCartMapper.class);

    AdminCartResponse cartToAdminCartResponse(Cart cart);

    @Mapping(target = "customer.id",source = "customer")
    Cart adminCartRequestToCart(AdminCartRequest request);

}
