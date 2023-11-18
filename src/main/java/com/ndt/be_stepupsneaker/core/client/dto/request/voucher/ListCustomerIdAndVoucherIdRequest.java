package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ListCustomerIdAndVoucherIdRequest {
    private List<UUID> voucher;
    private List<UUID> customer;
}
