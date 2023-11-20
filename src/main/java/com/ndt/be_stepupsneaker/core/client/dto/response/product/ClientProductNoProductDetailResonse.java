package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductNoProductDetailResonse {
    private String id;

    private String code;

    private String name;

    private ProductStatus status;

    private String image;
}