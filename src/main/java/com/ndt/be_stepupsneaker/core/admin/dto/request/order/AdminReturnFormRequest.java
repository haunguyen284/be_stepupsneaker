package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.RefundStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnDeliveryStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReturnFormRequest extends PageableRequest {

    private String id;

    private AdminAddressRequest address;

    private String order;

    private float amountToBePaid;

    private String paymentType;

    private String paymentInfo;

    private Set<AdminReturnFormDetailRequest> returnFormDetails;

    private RefundStatus refundStatus;

    private ReturnDeliveryStatus returnDeliveryStatus;

    private ReturnFormType type;

    private String note;

}
