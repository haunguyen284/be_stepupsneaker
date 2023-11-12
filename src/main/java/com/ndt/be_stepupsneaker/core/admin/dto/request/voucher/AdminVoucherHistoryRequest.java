package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminVoucherHistoryRequest extends PageableRequest {
    private String customer;
}
