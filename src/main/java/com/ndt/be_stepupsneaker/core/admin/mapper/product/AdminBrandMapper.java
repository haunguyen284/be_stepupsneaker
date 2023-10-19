package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminBrandMapper {
    AdminBrandMapper INSTANCE = Mappers.getMapper( AdminBrandMapper.class );

    AdminBrandResponse BrandToAdminBrandResponse(Brand Brand);

    Brand adminBrandRequestToBrand(AdminBrandRequest BrandDTO);
}
