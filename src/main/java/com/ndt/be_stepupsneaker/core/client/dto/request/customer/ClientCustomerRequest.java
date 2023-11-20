package com.ndt.be_stepupsneaker.core.client.dto.request.customer;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCustomerRequest extends PageableRequest {
    private String id;
    private String fullName;
    private String email;
    private Long dateOfBirth;
    private String password;
    private CustomerStatus status;
    private String gender;
    private String image;
}
