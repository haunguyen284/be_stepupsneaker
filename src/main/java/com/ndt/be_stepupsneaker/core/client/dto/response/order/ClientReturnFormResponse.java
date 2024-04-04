package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderNoOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientAddressResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.RefundStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnDeliveryStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientReturnFormResponse {

    private String id;

    private ClientOrderNoOrderDetailResponse order;

    private ClientAddressResponse address;

    private String code;

    private float amountToBePaid;

    private ReturnFormType type;

    private String paymentType;

    private String email;

    private String paymentInfo;

    private RefundStatus refundStatus;

    private Long createdAt;

    private ReturnDeliveryStatus returnDeliveryStatus;

    private List<ClientReturnFormDetailResponse> returnFormDetails;

    private List<ClientReturnFormHistoryResponse> returnFormHistories;


}
