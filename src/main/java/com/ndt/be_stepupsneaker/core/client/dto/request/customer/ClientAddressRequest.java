package com.ndt.be_stepupsneaker.core.client.dto.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
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

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String phoneNumber;

    private Boolean isDefault;

    private String districtId;

    private String provinceId;

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

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String height;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String length;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String weight;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String width;
}
