package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderDetailResponse {

    private UUID id;

    private ClientProductDetailResponse productDetail;

    private ClientOrderNoOrderDetailResponse order;

    private int quantity;

    private float price;

    private float totalPrice;

    private OrderStatus status;
}
