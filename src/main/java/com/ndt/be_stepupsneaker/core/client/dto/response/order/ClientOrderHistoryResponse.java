package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderHistoryResponse {

    private String id;

    private OrderStatus actionStatus;

    private String note;

    private Long createdAt;

}
