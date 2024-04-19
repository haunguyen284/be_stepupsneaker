package com.ndt.be_stepupsneaker.core.admin.dto.request.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
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

public class AdminProductRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "{product.code.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String code;

    @NotBlank(message = "{product.name.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String name;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String description;

    private String image;

    private String quantityMin;

    private String quantityMax;

    private String priceMin;

    private String priceMax;

    private ProductStatus status;

    private String tradeMark;

    private String style;

    private String size;

    private String product;

    private String material;

    private String color;

    private String brand;

    private String sole;

    private String start;

    private String end;

    private boolean hasPromotion;

    private List<String> tradeMarks;

    private List<String> styles;

    private List<String> sizes;

    private List<String> materials;

    private List<String> colors;

    private List<String> brands;

    private List<String> soles;
}

