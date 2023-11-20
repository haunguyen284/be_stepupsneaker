package com.ndt.be_stepupsneaker.core.admin.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleRsponse {
    private String id;
    private String name;
}
