package com.ndt.be_stepupsneaker.core.client.dto.request.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientPaymentMethodRequest extends PageableRequest {

    private String id;

    @NotBlank(message = "Name must not be null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String name;

}
