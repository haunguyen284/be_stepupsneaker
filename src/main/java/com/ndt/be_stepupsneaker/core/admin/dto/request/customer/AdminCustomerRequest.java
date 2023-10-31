package com.ndt.be_stepupsneaker.core.admin.dto.request.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import jakarta.persistence.OneToMany;
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
public class AdminCustomerRequest extends PageableRequest {
    private UUID id;
    private String fullName;
    private String email;
    private Long dateOfBirth;
    private String password;
    private CustomerStatus status;
    private String gender;
    private String image;
    List<AdminAddressRequest> adminAddressRequests;

    List<AdminCustomerVoucherRequest> customerVoucherList;
}
