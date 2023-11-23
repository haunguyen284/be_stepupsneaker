package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientStyleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientStyleResponse;
import com.ndt.be_stepupsneaker.entity.product.Style;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientStyleMapper {
    ClientStyleMapper INSTANCE = Mappers.getMapper( ClientStyleMapper.class );

    ClientStyleResponse colorToClientStyleResponse(Style style);

    Style clientStyleRequestToStyle(ClientStyleRequest clientStyleRequest);
}