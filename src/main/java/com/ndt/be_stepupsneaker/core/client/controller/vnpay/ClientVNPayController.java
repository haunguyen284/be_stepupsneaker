package com.ndt.be_stepupsneaker.core.client.controller.vnpay;

import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.client.service.vnpay.VNPayService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/transaction")
public class ClientVNPayController {

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/checkout")
    public Object submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo
    ) {
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo);
        return ResponseHelper.getResponse(vnpayUrl, HttpStatus.OK);
    }
    @GetMapping("/payment")
    public Object getOrderStatus(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setTransactionCode(request.getParameter("vnp_TransactionNo"));
        transactionInfo.setOrderInfo(request.getParameter("vnp_OrderInfo"));
        transactionInfo.setPaymentTime(request.getParameter("vnp_PayDate"));
        transactionInfo.setTotalPrice(request.getParameter("vnp_Amount"));
        transactionInfo.setPaymentStatus(paymentStatus);

        return ResponseHelper.getResponse(transactionInfo, HttpStatus.OK);
    }

}
