package com.ndt.be_stepupsneaker.core.client.dto.response.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientPromotionProductDetailResponse {
    private String id;
    //    private AdminPromotionResponse promotion;
    private ClientProductDetailResponse productDetail;
}
