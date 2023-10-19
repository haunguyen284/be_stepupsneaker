package com.ndt.be_stepupsneaker.core.admin.dto.response.product;

import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.entity.product.Material;
import com.ndt.be_stepupsneaker.entity.product.Product;
import com.ndt.be_stepupsneaker.entity.product.Size;
import com.ndt.be_stepupsneaker.entity.product.Sole;
import com.ndt.be_stepupsneaker.entity.product.Style;
import com.ndt.be_stepupsneaker.entity.product.TradeMark;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDetailResponse {
    private UUID id;

    private AdminTradeMarkResponse tradeMark;

    private AdminStyleResponse style;

    private AdminSizeResponse size;

    private AdminProductNoProductDetailResonse product;

    private AdminMaterialResponse material;

    private AdminColorResponse color;

    private AdminBrandResponse brand;

    private AdminSoleResponse sole;

    private String image;

    private float price;

    private int quantity;

    private ProductStatus status;
}