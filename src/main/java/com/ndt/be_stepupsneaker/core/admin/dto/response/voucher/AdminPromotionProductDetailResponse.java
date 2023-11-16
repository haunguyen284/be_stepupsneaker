package com.ndt.be_stepupsneaker.core.admin.dto.response.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPromotionProductDetailResponse {
    private UUID id;
    //    private AdminPromotionResponse promotion;
    private AdminProductDetailResponse productDetail;
}
