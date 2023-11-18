package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ClientProductMapper {

    ClientProductMapper INSTANCE = Mappers.getMapper( ClientProductMapper.class );

    ClientProductResponse productToClientProductResponse(Product product);

    Product clientProductRequestToProduct(ClientProductRequest productRequest);
}

