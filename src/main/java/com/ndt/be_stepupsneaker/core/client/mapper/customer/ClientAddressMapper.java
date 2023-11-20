package com.ndt.be_stepupsneaker.core.client.mapper.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientAddressResponse;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientAddressMapper {


    ClientAddressMapper INSTANCE = Mappers.getMapper(ClientAddressMapper.class);

    ClientAddressResponse addressToClientAddressResponse(Address address);

    @Mapping(target = "customer.id", source = "customer")
    Address clientAddressRequestAddress(ClientAddressRequest addressDTO);
}
