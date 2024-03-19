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

    @NotBlank(message = "{employee.full_name.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String fullName;

    @NotBlank(message = "{employee.email.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String email;

    @NotBlank(message = "{employee.password.not_blank}")
    private String password;

    @NotNull(message = "{employee.status.not_blank}")
    private EmployeeStatus status;

    @NotBlank(message = "{employee.address.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String address;

    @NotBlank(message = "{employee.gender.not_blank}")
    private String gender;

    @NotBlank(message = "{employee.phone.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String phoneNumber;

    private String image;

    @NotBlank(message = "{employee.role.not_blank}")
    private String role;
}
