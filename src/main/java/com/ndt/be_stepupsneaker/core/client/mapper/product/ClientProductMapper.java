package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ClientProductMapper {


    ClientProductMapper INSTANCE = Mappers.getMapper( ClientProductMapper.class );

    ClientProductResponse productToClientProductResponse(Product product);

    Product clientProductRequestToProduct(ClientProductRequest productRequest);

}

