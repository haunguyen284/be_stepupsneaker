package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCartItemRequest {
    private String id;
    private int quantity;
}
