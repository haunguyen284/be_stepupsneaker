package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;



@Mapper
public interface AdminProductMapper {

    AdminProductMapper INSTANCE = Mappers.getMapper( AdminProductMapper.class );

    AdminProductResponse productToAdminProductResponse(Product product);

    Product adminProductRequestToProduct(AdminProductRequest productRequest);
}

