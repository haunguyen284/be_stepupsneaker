package com.ndt.be_stepupsneaker.core.client.dto.request.payment;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClientPaymentMethodRequest extends PageableRequest {

    private UUID id;

    @NotBlank(message = "Name must not be null")
    private String name;

}
