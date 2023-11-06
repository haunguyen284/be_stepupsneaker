package com.ndt.be_stepupsneaker.core.admin.dto.response.order;

import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderHistoryResponse {

    private UUID id;

    private AdminOrderResponse order;

    private String actionDescription;

    private OrderStatus actionStatus;

    private String note;

    private Long createdAt;

}
