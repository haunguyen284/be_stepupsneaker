package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSoleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSoleResponse;
import com.ndt.be_stepupsneaker.entity.product.Sole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ClientSoleMapper {
    ClientSoleMapper INSTANCE = Mappers.getMapper( ClientSoleMapper.class );

    ClientSoleResponse colorToClientSoleResponse(Sole sole);

    Sole clientSoleRequestToSole(ClientSoleRequest request);
}