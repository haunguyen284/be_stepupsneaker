package com.ndt.be_stepupsneaker.core.client.dto.request.employee;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
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
public class ClientEmployeeRequest extends PageableRequest {
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private EmployeeStatus status;
    private String address;
    private String gender;
    private String phoneNumber;
    private String image;
    private UUID role;
}
