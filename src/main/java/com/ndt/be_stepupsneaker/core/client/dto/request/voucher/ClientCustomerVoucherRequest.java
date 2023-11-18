package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientCustomerVoucherRequest extends PageableRequest {
    private UUID id;

    private UUID voucher;

    private UUID customer;
}
