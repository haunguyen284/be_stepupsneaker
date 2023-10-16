package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminColorResponse;
import com.ndt.be_stepupsneaker.entity.product.Color;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminColorMapper {

    AdminColorMapper INSTANCE = Mappers.getMapper( AdminColorMapper.class );

    AdminColorResponse colorToAdminColorResponse(Color color);

    Color adminColorRequestToColor(AdminColorRequest colorDTO);
}
