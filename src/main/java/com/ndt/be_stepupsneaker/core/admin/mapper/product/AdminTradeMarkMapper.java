package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminTradeMarkResponse;
import com.ndt.be_stepupsneaker.entity.product.TradeMark;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminTradeMarkMapper {
    AdminTradeMarkMapper INSTANCE = Mappers.getMapper( AdminTradeMarkMapper.class );

    AdminTradeMarkResponse colorToAdminTradeMarkResponse(TradeMark color);

    TradeMark adminTradeMarkRequestToTradeMark(AdminTradeMarkRequest colorDTO);
}