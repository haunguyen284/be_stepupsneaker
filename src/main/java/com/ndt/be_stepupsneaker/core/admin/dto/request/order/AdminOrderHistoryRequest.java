package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminOrderHistoryRequest extends PageableRequest {

    private String id;

    private String order;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String actionDescription;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String note;

}
