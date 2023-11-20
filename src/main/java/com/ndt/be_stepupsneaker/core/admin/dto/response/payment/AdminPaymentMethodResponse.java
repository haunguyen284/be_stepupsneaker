package com.ndt.be_stepupsneaker.core.admin.dto.response.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminPaymentMethodResponse {

    private String id;

    private String name;

}
