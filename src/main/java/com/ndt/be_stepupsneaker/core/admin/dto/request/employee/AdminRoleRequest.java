package com.ndt.be_stepupsneaker.core.admin.dto.request.employee;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleRequest  extends PageableRequest {
    private UUID id;

    @NotBlank(message = "Name must be not null")
    private String name;
}
