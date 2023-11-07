package com.ndt.be_stepupsneaker.core.admin.dto.response.employee;

import com.ndt.be_stepupsneaker.infrastructure.constant.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmployeeResponse {
    private UUID id;
    private String fullName;
    private AdminRoleRsponse role;
    private String email;
    private String password;
    private EmployeeStatus status;
    private String address;
    private String gender;
    private String phoneNumber;
    private String image;
}
