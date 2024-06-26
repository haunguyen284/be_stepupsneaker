package com.ndt.be_stepupsneaker.core.client.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.validation.constraints.NotBlank;
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

public class ClientProductRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "Code must be not null")
    private String code;

    @NotBlank(message = "Name must be not null")
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

