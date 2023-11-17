package com.ndt.be_stepupsneaker.core.client.dto.response.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCartResponse {
    private UUID id;
    private ClientCustomerResponse customer;
}
