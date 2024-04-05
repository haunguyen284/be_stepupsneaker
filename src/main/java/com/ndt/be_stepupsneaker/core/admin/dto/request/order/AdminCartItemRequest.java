package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCartItemRequest {

    @NotNull(message = "{cart_item.id.not_null}")
    private String id;

    @NotNull(message = "{cart_item.quantity.not_null}")
    private int quantity;
}
