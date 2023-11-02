package com.ndt.be_stepupsneaker.core.admin.dto.response.order;

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

    private String note;

}
