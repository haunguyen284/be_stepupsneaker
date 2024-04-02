package com.ndt.be_stepupsneaker.core.client.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientReturnFormDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientReturnFormDetailResponse;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientReturnFormDetailMapper {
    ClientReturnFormDetailMapper INSTANCE = Mappers.getMapper( ClientReturnFormDetailMapper.class );
    ClientReturnFormDetailResponse returnFormDetailToClientReturnFormDetailResponse(ReturnFormDetail returnFormDetail);

    @Mapping(target = "orderDetail.id", source = "orderDetail")
    ReturnFormDetail clientReturnFormDetailRequestToReturnFormDetail(ClientReturnFormDetailRequest returnFormDetailRequest);
}