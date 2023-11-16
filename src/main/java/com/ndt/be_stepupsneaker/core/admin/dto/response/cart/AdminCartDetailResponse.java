package com.ndt.be_stepupsneaker.core.admin.dto.response.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCartDetailResponse {
    private UUID id;
    private AdminCartResponse cart;
    private AdminProductDetailResponse productDetail;
    private int quantity;
}
