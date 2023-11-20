package com.ndt.be_stepupsneaker.core.client.dto.request.product;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientMaterialRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "Name must be not null")
    private String name;

    @NotNull(message = "Status must be not null")
    private ProductPropertiesStatus status;
}
