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
public class AdminMaterialResponse {
    private String id;
    private String name;
    private ProductPropertiesStatus status;
}
