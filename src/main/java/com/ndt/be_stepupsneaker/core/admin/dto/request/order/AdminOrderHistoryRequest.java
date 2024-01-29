package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminOrderHistoryRequest extends PageableRequest {

    private String id;

    private String order;

    private String actionDescription;

    private String note;

}
