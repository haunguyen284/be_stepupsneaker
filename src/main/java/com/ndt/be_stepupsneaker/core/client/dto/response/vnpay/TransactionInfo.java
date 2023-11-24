package com.ndt.be_stepupsneaker.core.client.dto.response.vnpay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInfo {

    private String orderInfo;

    private String paymentTime;

    private String transactionCode;

    private String totalPrice;

    private Integer paymentStatus;

}
