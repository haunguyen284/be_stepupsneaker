package com.ndt.be_stepupsneaker.core.client.dto.response.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientPromotionNoProductDetailResponse {

    private String id;

    private ClientPromotionNoPromotionProductDetailResponse promotion;
}
