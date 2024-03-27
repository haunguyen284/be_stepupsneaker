package com.ndt.be_stepupsneaker.core.admin.dto.response.order;

import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReturnFormResponse {

    private String id;

    private AdminOrderResponse order;

    private String reason;

    private String feedback;

    private ReturnFormStatus status;
}
