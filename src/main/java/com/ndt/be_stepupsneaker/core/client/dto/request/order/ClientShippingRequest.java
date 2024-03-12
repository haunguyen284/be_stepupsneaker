package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientShippingRequest {

    @JsonProperty("from_district_id")
    private int fromDistrictId;

    @JsonProperty("to_district_id")
    private int toDistrictId;

    @JsonProperty("to_ward_code")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String toWardCode;

    @JsonProperty("service_id")
    private int serviceId;

    @JsonProperty("height")
    private int height;

    @JsonProperty("length")
    private int length;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("width")
    private int width;

}
