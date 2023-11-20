package com.ndt.be_stepupsneaker.core.admin.dto.response.product;

import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductNoProductDetailResonse {
    private String id;

    private String code;

    private String name;

    private ProductStatus status;

    private String image;
}