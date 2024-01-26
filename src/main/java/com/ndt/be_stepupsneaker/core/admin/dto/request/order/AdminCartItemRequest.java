package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCartItemRequest {

    @NotNull(message = "Id must be not null")
    private String id;

    @NotNull(message = "Quantity must be not null")
    private int quantity;
}
