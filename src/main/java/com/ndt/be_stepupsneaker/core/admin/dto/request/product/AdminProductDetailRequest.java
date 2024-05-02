package com.ndt.be_stepupsneaker.core.admin.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

public class AdminProductDetailRequest extends PageableRequest {
    private String id;

    @NotNull(message = "{product_detail.trade_mark.not_blank}")
    private String tradeMark;

    @NotNull(message = "{product_detail.style.not_blank}")
    private String style;

    @NotNull(message = "{product_detail.size.not_blank}")
    private String size;

    @NotNull(message = "{product_detail.product.not_blank}")
    private String product;

    @NotNull(message = "{product_detail.material.not_blank}")
    private String material;

    @NotNull(message = "{product_detail.color.not_blank}")
    private String color;

    @NotNull(message = "{product_detail.brand.not_blank}")
    private String brand;

    @NotNull(message = "{product_detail.sole.not_blank}")
    private String sole;

    private String promotion;

    private int isInPromotion=1;

    private String hasPromotion;

    private String image;

    @Min(value = 1, message = "{product_detail.price.min}")
    private float price;

    private String priceMin;

    private String priceMax;

    @Min(value = 1, message = "{product_detail.quantity.min}")
    private int quantity;

    private String quantityMin;

    private String quantityMax;

    private ProductStatus status;

    private List<String> tradeMarks;

    private List<String> styles;

    private List<String> sizes;

    private List<String> materials;

    private List<String> colors;

    private List<String> brands;

    private List<String> soles;

    private List<String> products;
}

