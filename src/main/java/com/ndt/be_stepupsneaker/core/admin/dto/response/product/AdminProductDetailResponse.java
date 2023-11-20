package com.ndt.be_stepupsneaker.core.admin.dto.response.product;

import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionNoProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDetailResponse {
    private String id;

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

    List<AdminPromotionNoProductDetailResponse> promotionProductDetails;

}
