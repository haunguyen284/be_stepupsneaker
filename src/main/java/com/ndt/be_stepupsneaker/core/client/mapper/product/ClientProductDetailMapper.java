package com.ndt.be_stepupsneaker.core.client.mapper.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientProductDetailMapper {
    ClientProductDetailMapper INSTANCE = Mappers.getMapper( ClientProductDetailMapper.class );

    ClientProductDetailResponse productDetailToClientProductDetailResponse(ProductDetail productDetail);

    @Mapping(target = "tradeMark.id", source = "tradeMark")
    @Mapping(target = "style.id", source = "style")
    @Mapping(target = "size.id", source = "size")
    @Mapping(target = "product.id", source = "product")
    @Mapping(target = "material.id", source = "material")
    @Mapping(target = "color.id", source = "color")
    @Mapping(target = "brand.id", source = "brand")
    @Mapping(target = "sole.id", source = "sole")
    ProductDetail clientProductDetailRequestToProductDetail(ClientProductDetailRequest productDetailRequest);

}
