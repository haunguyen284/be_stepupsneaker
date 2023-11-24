package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductResponse {

    private String id;
    private String code;

    private String name;

    private String description;

    private String image;

    private ProductStatus status;

    private Long saleCount;

    private Set<ClientProductDetailResponse> productDetails;

    private Long createdAt;

}
