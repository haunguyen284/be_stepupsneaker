package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSoleResponse;
import com.ndt.be_stepupsneaker.entity.product.Sole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AdminSoleMapper {
    AdminSoleMapper INSTANCE = Mappers.getMapper( AdminSoleMapper.class );

    AdminSoleResponse colorToAdminSoleResponse(Sole color);

    Sole adminSoleRequestToSole(AdminSoleRequest colorDTO);
}