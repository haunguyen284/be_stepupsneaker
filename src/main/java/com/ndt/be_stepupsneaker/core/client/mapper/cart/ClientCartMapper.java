package com.ndt.be_stepupsneaker.core.client.mapper.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartResponse;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientCartMapper {
    ClientCartMapper INSTANCE = Mappers.getMapper(ClientCartMapper.class);

    ClientCartResponse cartToClientCartResponse(Cart cart);

    @Mapping(target = "customer.id",source = "customer")
    Cart clientCartRequestToCart(ClientCartRequest request);

}
