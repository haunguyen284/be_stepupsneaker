package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientSizeQuantityResponse {

    private String size ;
    private int quantity;
}
