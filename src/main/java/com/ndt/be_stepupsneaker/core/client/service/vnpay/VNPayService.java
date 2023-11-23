package com.ndt.be_stepupsneaker.core.client.service.vnpay;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {

    String createOrder(int total, String orderInfor);
    int orderReturn(HttpServletRequest request);

}
