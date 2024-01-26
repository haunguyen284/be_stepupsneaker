package com.ndt.be_stepupsneaker.core.admin.dto.request.customer;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddressRequest extends PageableRequest {
    private String id;
    @NotBlank(message = "PhoneNumber must be not null")
    private String phoneNumber;

    private Boolean isDefault;
    private String districtId;
    private String provinceId;
    private String wardCode;
    private String districtName;
    private String provinceName;
    private String wardName;

    @NotBlank(message = "More must be not null")
    private String more;
    private String customer;
}
