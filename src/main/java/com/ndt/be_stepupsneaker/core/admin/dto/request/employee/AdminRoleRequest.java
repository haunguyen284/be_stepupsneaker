package com.ndt.be_stepupsneaker.core.admin.dto.request.employee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
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
    private String id;

    @NotBlank(message = "{role.name.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String name;
}
