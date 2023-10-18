package com.ndt.be_stepupsneaker.core.admin.dto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddressResponse {
    private UUID id;
    private String phoneNumber;
    private Boolean isDefault;
    private String city;
    private String province;
    private String country;
}
