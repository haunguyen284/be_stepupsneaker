package com.ndt.be_stepupsneaker.core.admin.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormDetailResponse;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminReturnFormDetailMapper {
    AdminReturnFormDetailMapper INSTANCE = Mappers.getMapper( AdminReturnFormDetailMapper.class );
    AdminReturnFormDetailResponse returnFormDetailToAdminReturnFormDetailResponse(ReturnFormDetail returnFormDetail);

    @Mapping(target = "orderDetail.id", source = "orderDetail")
    ReturnFormDetail adminReturnFormDetailRequestToReturnFormDetail(AdminReturnFormDetailRequest returnFormDetailRequest);
}