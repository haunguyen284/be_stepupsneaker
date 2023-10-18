package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminStyleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminStyleResponse;
import com.ndt.be_stepupsneaker.entity.product.Style;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminStyleMapper {
    AdminStyleMapper INSTANCE = Mappers.getMapper( AdminStyleMapper.class );

    AdminStyleResponse colorToAdminStyleResponse(Style color);

    Style adminStyleRequestToStyle(AdminStyleRequest colorDTO);
}