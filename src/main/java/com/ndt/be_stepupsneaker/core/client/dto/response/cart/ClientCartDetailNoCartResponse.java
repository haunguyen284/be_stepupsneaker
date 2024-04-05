package com.ndt.be_stepupsneaker.core.client.dto.response.cart;

import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCartDetailNoCartResponse {

    private String id;

    private ClientProductDetailResponse productDetail;

    private int quantity;

    private Long createdAt;
}
