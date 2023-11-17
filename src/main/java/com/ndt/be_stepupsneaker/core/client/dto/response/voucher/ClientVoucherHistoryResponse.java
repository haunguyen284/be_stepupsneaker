package com.ndt.be_stepupsneaker.core.client.dto.response.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderNoOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderNoOrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientVoucherHistoryResponse {
    private ClientOrderNoOrderDetailResponse order;

    private ClientVoucherResponse voucher;

    private float moneyBeforeReduction;

    private float moneyAfterReduction;

    private float moneyReduction;

    private Long createdAt;
}
