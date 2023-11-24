package com.ndt.be_stepupsneaker.core.client.service.vnpay;

import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.entity.order.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface VNPayService {

    String createOrder(int total, String orderInfor);
    Order authenticateVnPay(HttpServletRequest request);

}
