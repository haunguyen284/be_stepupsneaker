package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommonRequest {
    //customerVoucher
    private String voucher;
    private List<String> customers;

    //promotionProductDetail
    private String promotion;
    private List<String> productDetails;
}
