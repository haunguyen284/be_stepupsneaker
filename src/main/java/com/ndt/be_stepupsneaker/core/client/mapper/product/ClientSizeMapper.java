package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSizeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSizeResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSizeRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSizeResponse;
import com.ndt.be_stepupsneaker.entity.product.Size;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientSizeMapper {

    ClientSizeMapper INSTANCE = Mappers.getMapper( ClientSizeMapper.class );

    ClientSizeResponse sizeToAdminSizeResponse(Size size);

    Size clientSizeRequestToSize(ClientSizeRequest clientSizeRequest);
}
