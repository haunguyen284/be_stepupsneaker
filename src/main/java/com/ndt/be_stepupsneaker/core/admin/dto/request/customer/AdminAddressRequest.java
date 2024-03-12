package com.ndt.be_stepupsneaker.core.admin.dto.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
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

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String phoneNumber;

    private Boolean isDefault;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String districtId;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String provinceId;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String wardCode;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String districtName;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String provinceName;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String wardName;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String more;

    private String customer;
}
