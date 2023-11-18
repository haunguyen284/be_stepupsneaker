package com.ndt.be_stepupsneaker.core.client.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRoleResponse {
    private UUID id;
    private String name;
}
