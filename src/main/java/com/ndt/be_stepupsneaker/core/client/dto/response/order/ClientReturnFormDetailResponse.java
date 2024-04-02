package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnInspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientReturnFormDetailResponse {

    private String id;

    private ClientOrderDetailResponse orderDetail;

    private int quantity;

    private String reason;

    private String feedback;

    private ReturnInspectionStatus returnInspectionStatus;

    private String returnInspectionReason;

    private String urlImage;

    private boolean resellable;
}
