package com.ndt.be_stepupsneaker.core.admin.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
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

public class AdminColorRequest extends PageableRequest {
    private UUID id;
    private String code;
    private String name;
    private ProductPropertiesStatus status;
}
