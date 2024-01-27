package com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDetailResponse<T> {
    private T oldValue;
    private T newValue;
}
