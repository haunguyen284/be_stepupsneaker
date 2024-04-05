package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnInspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReturnFormDetailRequest extends PageableRequest {
    private String id;

    private String orderDetail;

    private int quantity;

    private String reason;

    private String feedback;

    private ReturnInspectionStatus returnInspectionStatus;

    private String returnInspectionReason;

    private String image;

    private boolean resellable;

}
