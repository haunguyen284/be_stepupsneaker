package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminMaterialRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminMaterialResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientMaterialRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientMaterialResponse;
import com.ndt.be_stepupsneaker.entity.product.Material;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMaterialMapper {
    ClientMaterialMapper INSTANCE = Mappers.getMapper( ClientMaterialMapper.class );

    ClientMaterialResponse colorToClientMaterialResponse(Material material);

    Material clientMaterialRequestToMaterial(ClientMaterialRequest request);
}
