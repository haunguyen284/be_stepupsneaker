package com.ndt.be_stepupsneaker.core.client.dto.request.customer;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
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
    private String phoneNumber;
    private Boolean isDefault;
    private String districtId;
    private String provinceId;
    private String wardCode;
    private String districtName;
    private String provinceName;
    private String wardName;
    private String more;
    private String customer;
    private int height;
    private int length;
    private int weight;
    private int width;
}
