package com.ndt.be_stepupsneaker.core.admin.dto.response.product;

import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminColorResponse {
    private String id;
    private String code;
    private String name;
    private ProductPropertiesStatus status;
}
