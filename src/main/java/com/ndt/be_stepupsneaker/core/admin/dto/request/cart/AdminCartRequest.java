package com.ndt.be_stepupsneaker.core.admin.dto.request.cart;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCartRequest extends PageableRequest {
    private UUID id;
    private UUID customer;
}
