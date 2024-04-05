package com.ndt.be_stepupsneaker.core.admin.dto.request.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminPaymentMethodRequest extends PageableRequest {

    private String id;

    @NotBlank(message = "{payment_method.name.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String name;

}
