package com.ndt.be_stepupsneaker.core.admin.dto.response.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderNoOrderDetailResponse;
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
    private AdminOrderNoOrderDetailResponse order;

    private AdminVoucherResponse voucher;

    private float moneyBeforeReduction;

    private float moneyAfterReduction;

    private float moneyReduction;

    private Long createdAt;

    private Long updatedAt;
}
