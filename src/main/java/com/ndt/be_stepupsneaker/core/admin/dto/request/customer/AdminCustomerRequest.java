package com.ndt.be_stepupsneaker.core.admin.dto.request.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCustomerRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "PhoneNumber must be not null")
    private String fullName;

    @NotBlank(message = "Email must be not null")
    private String email;

    @NotNull(message = "DateOfBirth must be not null")
    private Long dateOfBirth;

    @NotBlank(message = "Password must be not null")
    private String password;

    @NotBlank(message = "Status must be not null")
    private CustomerStatus status;

    @NotBlank(message = "Gender must be not null")
    private String gender;

    private String image;
}
