package com.ndt.be_stepupsneaker.core.admin.dto.request.employee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.EmployeeStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmployeeRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "FullName must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String fullName;

    @NotBlank(message = "Email must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String email;

    @NotBlank(message = "Password must be not null")
    private String password;

    @NotNull(message = "Status must be not null")
    private EmployeeStatus status;

    @NotBlank(message = "Address must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String address;

    @NotBlank(message = "Gender must be not null")
    private String gender;

    @NotBlank(message = "PhoneNumber must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String phoneNumber;

    private String image;

    @NotBlank(message = "Role must be not null")
    private String role;
}
