package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminReturnFormHistoryResponse {

    private ReturnFormStatus actionStatus;

    private String note;
}
