package com.ndt.be_stepupsneaker.core.client.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientProductDetailRequest extends PageableRequest {
    private String id;

    private String tradeMark;

    private String style;

    private String size;

    private String product;

    private String material;

    private String color;

    private String brand;

    private String sole;

    private String promotion;

    private int isInPromotion=1;

    private String image;

    private float price;

    private String priceMin;

    private String priceMax;

    private int quantity;

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

