package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientBrandRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientBrandResponse;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientBrandMapper {
    ClientBrandMapper INSTANCE = Mappers.getMapper( ClientBrandMapper.class );

    ClientBrandResponse brandToClientBrandResponse(Brand Brand);

    Brand clientBrandRequestToBrand(ClientBrandRequest BrandDTO);
}
