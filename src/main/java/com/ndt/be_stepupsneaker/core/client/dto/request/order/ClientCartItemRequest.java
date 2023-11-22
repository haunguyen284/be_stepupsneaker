package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCartItemRequest {
    private String id;
    private int quantity;
}
