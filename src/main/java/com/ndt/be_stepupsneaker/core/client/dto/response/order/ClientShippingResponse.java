package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientShippingResponse {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("service_fee")
    private float fee;
}
