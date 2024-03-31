package com.ndt.be_stepupsneaker.core.admin.dto.response.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormDetail;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
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
public class AdminReturnFormResponse {

    private String id;

    private AdminOrderNoOrderDetailResponse order;

    private AdminEmployeeResponse employee;

    private AdminAddressResponse address;


    private String code;

    private float amountToBePaid;

    private ReturnFormType type;

    private String paymentType;

    private String paymentInfo;

    private RefundStatus refundStatus;

    private ReturnDeliveryStatus returnDeliveryStatus;



    private List<AdminReturnFormDetailResponse> returnFormDetails;

    private List<AdminReturnFormHistoryResponse> returnFormHistories;
}
