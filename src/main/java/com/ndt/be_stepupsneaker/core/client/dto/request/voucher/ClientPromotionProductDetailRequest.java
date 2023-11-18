package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

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
public class ClientPromotionProductDetailRequest extends PageableRequest {
    private UUID id;

    private UUID promotion;

    private UUID productDetail;

    private List<UUID> productDetails;
}
