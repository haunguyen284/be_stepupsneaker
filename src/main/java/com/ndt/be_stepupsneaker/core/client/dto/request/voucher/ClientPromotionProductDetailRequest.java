package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientPromotionProductDetailRequest extends PageableRequest {
    private String id;

    private String promotion;

    private String productDetail;

    private List<String> productDetails;
}
