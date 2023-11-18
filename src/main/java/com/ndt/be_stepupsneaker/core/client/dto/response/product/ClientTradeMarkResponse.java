package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientTradeMarkResponse {
    private UUID id;
    private String name;
    private ProductPropertiesStatus status;
}