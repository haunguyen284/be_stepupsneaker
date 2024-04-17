package com.ndt.be_stepupsneaker.core.admin.dto.request.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminSizeRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "{size.name.not_blank}")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    @Range(min = 33, max = 44, message = "Size giày phải có kích cỡ từ 33 đến 44")
    private String name;

    private ProductPropertiesStatus status;
}
