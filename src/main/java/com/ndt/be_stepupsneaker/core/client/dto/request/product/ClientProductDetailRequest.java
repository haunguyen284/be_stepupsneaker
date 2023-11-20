package com.ndt.be_stepupsneaker.core.client.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientProductDetailRequest extends PageableRequest {
    private String id;

    @NotNull(message = "Trade mark must be not null")
    private String tradeMark;

    @NotNull(message = "style must be not null")
    private String style;

    @NotNull(message = "Size must be not null")
    private String size;

    @NotNull(message = "Product must be not null")
    private String product;

    @NotNull(message = "Material must be not null")
    private String material;

    @NotNull(message = "Color must be not null")
    private String color;

    @NotNull(message = "Brand must be not null")
    private String brand;

    @NotNull(message = "Sole must be not null")
    private String sole;

    private String promotion;

    private int isInPromotion=1;

    private String image;

    private float price;

    private float priceMin;

    private float priceMax;

    private int quantity;

    private ProductStatus status;
}

