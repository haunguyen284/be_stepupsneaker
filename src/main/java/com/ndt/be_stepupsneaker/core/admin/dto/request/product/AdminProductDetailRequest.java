package com.ndt.be_stepupsneaker.core.admin.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminProductDetailRequest extends PageableRequest {
    private UUID id;

    @NotNull(message = "Trade mark must be not null")
    private UUID tradeMark;

    @NotNull(message = "style must be not null")
    private UUID style;

    @NotNull(message = "Size must be not null")
    private UUID size;

    @NotNull(message = "Product must be not null")
    private UUID product;

    @NotNull(message = "Material must be not null")
    private UUID material;

    @NotNull(message = "Color must be not null")
    private UUID color;

    @NotNull(message = "Brand must be not null")
    private UUID brand;

    @NotNull(message = "Sole must be not null")
    private UUID sole;

    private String promotion;

    private String noPromotion;

    private String image;

    private float price;

    private float priceMin;

    private float priceMax;

    private int quantity;

    private ProductStatus status;
}

