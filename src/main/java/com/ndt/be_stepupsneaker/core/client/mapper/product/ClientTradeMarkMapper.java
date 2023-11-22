package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientTradeMarkResponse;
import com.ndt.be_stepupsneaker.entity.product.TradeMark;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientTradeMarkMapper {
    ClientTradeMarkMapper INSTANCE = Mappers.getMapper( ClientTradeMarkMapper.class );

    ClientTradeMarkResponse tradeMarkToClientTradeMarkResponse(TradeMark tradeMark);

    TradeMark clientTradeMarkRequestToTradeMark(ClientTradeMarkRequest request);
}