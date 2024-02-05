package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionNoProductDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientProductDetailResponse {
    private String id;

    private ClientTradeMarkResponse tradeMark;

    private ClientStyleResponse style;

    private ClientSizeResponse size;

    private ClientProductNoProductDetailResonse product;

    private ClientMaterialResponse material;

    private ClientColorResponse color;

    private ClientBrandResponse brand;

    private ClientSoleResponse sole;

    private String image;

    private float price;

    private int quantity;

    private float moneyPromotion;

    private ProductStatus status;

    List<AdminPromotionNoProductDetailResponse> promotionProductDetails;

}
