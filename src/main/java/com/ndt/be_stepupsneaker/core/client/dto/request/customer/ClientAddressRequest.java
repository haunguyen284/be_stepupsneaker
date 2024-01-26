package com.ndt.be_stepupsneaker.core.client.dto.request.customer;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientAddressRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "PhoneNumber must be not null")
    private String phoneNumber;
    private Boolean isDefault;

    @NotBlank(message = "DistrictId must be not null")
    private String districtId;

    @NotBlank(message = "ProvinceId must be not null")
    private String provinceId;

    @NotBlank(message = "WardCode must be not null")
    private String wardCode;
    private String districtName;
    private String provinceName;
    private String wardName;

    @NotBlank(message = "More must be not null")
    private String more;
    private String customer;

    private String height;
    private String length;
    private String weight;
    private String width;
}
