package com.ndt.be_stepupsneaker.core.client.dto.response.vnpay;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionInfo {

    private String orderInfo;

    private String paymentTime;

    private String transactionCode;

    private String totalPrice;

    private Integer paymentStatus;

}
