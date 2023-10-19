package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ListCustomerAndVoucher {
    private List<AdminVoucherRequest> voucherRequestList;
    private List<AdminCustomerRequest> customerRequestList;
}
