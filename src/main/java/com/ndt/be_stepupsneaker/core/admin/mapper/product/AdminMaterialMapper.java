package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminMaterialRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminMaterialResponse;
import com.ndt.be_stepupsneaker.entity.product.Material;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMaterialMapper {
    AdminMaterialMapper INSTANCE = Mappers.getMapper( AdminMaterialMapper.class );

    AdminMaterialResponse colorToAdminMaterialResponse(Material color);

    Material adminMaterialRequestToMaterial(AdminMaterialRequest colorDTO);
}
