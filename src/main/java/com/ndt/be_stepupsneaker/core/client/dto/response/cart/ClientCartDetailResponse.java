package com.ndt.be_stepupsneaker.core.client.dto.response.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCartDetailResponse {
    private UUID id;
    private ClientCartResponse cart;
    private ClientProductDetailResponse productDetail;
    private int quantity;
}
