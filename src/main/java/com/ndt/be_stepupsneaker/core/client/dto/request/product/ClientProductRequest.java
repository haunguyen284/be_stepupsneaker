package com.ndt.be_stepupsneaker.core.client.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.validation.constraints.NotBlank;
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

public class ClientProductRequest extends PageableRequest {
    private UUID id;

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
}

