package com.ndt.be_stepupsneaker.core.client.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCartResponse {

    private String id;

    private List<ClientCartDetailNoCartResponse> cartDetails;
}
