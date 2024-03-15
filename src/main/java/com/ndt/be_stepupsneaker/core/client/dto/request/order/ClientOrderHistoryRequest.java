package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientOrderHistoryRequest extends PageableRequest {

    private String id;

    @NotBlank(message = "Order must be not null")
    private String order;

    @NotBlank(message = "Action description must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String actionDescription;

    @NotBlank(message = "Note must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String note;

}
