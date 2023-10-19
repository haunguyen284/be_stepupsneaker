package com.ndt.be_stepupsneaker.core.admin.mapper.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminProductDetailMapper {
    AdminProductDetailMapper INSTANCE = Mappers.getMapper( AdminProductDetailMapper.class );

    AdminProductDetailResponse productDetailToAdminProductDetailResponse(ProductDetail productDetail);

    @Mapping(target = "tradeMark.id", source = "tradeMark")
    @Mapping(target = "style.id", source = "style")
    @Mapping(target = "size.id", source = "size")
    @Mapping(target = "product.id", source = "product")
    @Mapping(target = "material.id", source = "material")
    @Mapping(target = "color.id", source = "color")
    @Mapping(target = "brand.id", source = "brand")
    @Mapping(target = "sole.id", source = "sole")
    ProductDetail adminProductDetailRequestToProductDetail(AdminProductDetailRequest productDetailRequest);

}
