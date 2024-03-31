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
public class AdminReturnFormHistoryResponse {

    private String id;

    private ReturnFormStatus actionStatus;

    private String note;

    private Long createdAt;

}
