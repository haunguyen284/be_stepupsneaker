package com.ndt.be_stepupsneaker.core.admin.dto.response.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
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
public class AdminCustomerResponse {
    private UUID id;
    private String fullName;
    private String email;
    private Long dateOfBirth;
    private String password;
    private CustomerStatus status;
    private String gender;
    private String image;
    List<AdminCustomerVoucherResponse> adminCustomerVoucherResp;
}
