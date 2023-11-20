package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientColorRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientColorResponse;
import com.ndt.be_stepupsneaker.entity.product.Color;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientColorMapper {

    ClientColorMapper INSTANCE = Mappers.getMapper( ClientColorMapper.class );

    ClientColorResponse colorToClientColorResponse(Color color);

    Color clientColorRequestToColor(ClientColorRequest colorDTO);
}
