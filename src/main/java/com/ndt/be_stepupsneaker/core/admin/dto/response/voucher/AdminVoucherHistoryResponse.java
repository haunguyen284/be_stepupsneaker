package com.ndt.be_stepupsneaker.core.admin.dto.response.voucher;

import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminVoucherHistoryResponse {
    private Order order;

    private Voucher voucher;

    private float moneyBeforeReduction;

    private float moneyAfterReduction;

    private float moneyReduction;
}
