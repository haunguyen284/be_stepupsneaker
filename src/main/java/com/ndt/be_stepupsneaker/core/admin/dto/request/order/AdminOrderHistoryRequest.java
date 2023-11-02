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

    private UUID id;

    @NotBlank(message = "Order must be not null")
    private UUID order;

    @NotBlank(message = "Action description must be not null")
    private String actionDescription;

    @NotBlank(message = "Note must be not null")
    private String note;

}
