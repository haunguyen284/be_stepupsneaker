package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientShippingDataResponse {

    @JsonProperty("service_fee")
    private float service_fee;

    @JsonProperty("total")
    private int total;

}
