package com.ndt.be_stepupsneaker.core.client.dto.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "FullName must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String fullName;

    @NotBlank(message = "Email must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String email;

    @NotNull(message = "DateOfBirt must be not null")
    private Long dateOfBirth;

    private String password;

    private CustomerStatus status;

    @NotBlank(message = "Gender must be not null")
    private String gender;

    private String image;
}
