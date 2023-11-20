package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListCustomerIdAndVoucherIdRequest {
    private List<String> voucher;
    private List<String> customer;
}
