package com.ndt.be_stepupsneaker.core.client.dto.response.customer;

import com.ndt.be_stepupsneaker.entity.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientAddressResponse {
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
    private ClientCustomerResponse customer;
}
