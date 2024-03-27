package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReturnFormRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "{return_form.order.not_blank}")
    private String orderCode;

    private String reason;

    private String feedback;

    private ReturnFormStatus status;
}
