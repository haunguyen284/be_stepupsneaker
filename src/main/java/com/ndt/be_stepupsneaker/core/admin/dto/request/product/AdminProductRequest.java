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

    @NotBlank(message = "Code must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String code;

    @NotBlank(message = "Name must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String name;

    private String description;

    private String image;

    private String minQuantity;

    private String maxQuantity;

    @NotNull(message = "Status must be not null")
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

    private List<String> tradeMarks;

    private List<String> styles;

    private List<String> sizes;

    private List<String> materials;

    private List<String> colors;

    private List<String> brands;

    private List<String> soles;
}

