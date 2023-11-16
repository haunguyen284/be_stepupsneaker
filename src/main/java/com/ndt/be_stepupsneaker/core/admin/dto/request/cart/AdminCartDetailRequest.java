package com.ndt.be_stepupsneaker.core.admin.dto.request.cart;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCartDetailRequest extends PageableRequest {
    private UUID id;
    private UUID cart;
    private UUID productDetail;
    private int quantity;
    private List<UUID> cartDetails;
    private UUID customer;
}
