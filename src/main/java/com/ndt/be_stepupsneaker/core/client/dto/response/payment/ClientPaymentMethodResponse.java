package com.ndt.be_stepupsneaker.core.client.dto.response.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClientPaymentMethodResponse {

    private UUID id;

    private String name;

}
