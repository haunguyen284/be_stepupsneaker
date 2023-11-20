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
public class AdminPromotionNoProductDetailResponse {

    private String id;

    private AdminPromotionNoPromotionProductDetailResponse promotion;
}
