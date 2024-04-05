package com.ndt.be_stepupsneaker.core.admin.dto.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
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

    @NotBlank(message = "{customer.full_name.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String fullName;

    @NotBlank(message = "{customer.email.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String email;

    @NotNull(message = "{customer.dob.not_blank}")
    private Long dateOfBirth;

    private String password;

    private CustomerStatus status;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String gender;

    private String image;

    private AdminAddressRequest address;
}
