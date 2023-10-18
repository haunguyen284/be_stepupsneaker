package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSizeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminColorResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSizeResponse;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.entity.product.Size;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminSizeMapper {

    AdminSizeMapper INSTANCE = Mappers.getMapper( AdminSizeMapper.class );

    AdminSizeResponse sizeToAdminSizeResponse(Size size);

    Size adminSizeRequestToSize(AdminSizeRequest adminSizeRequest);
}
