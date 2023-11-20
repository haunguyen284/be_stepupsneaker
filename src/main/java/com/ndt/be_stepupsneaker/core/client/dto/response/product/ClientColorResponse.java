package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientColorResponse {
    private String id;
    private String code;
    private String name;
    private ProductPropertiesStatus status;
}
