package com.ndt.be_stepupsneaker.core.client.controller.vnpay;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.client.service.impl.order.ClientOrderServiceImpl;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.client.service.vnpay.VNPayService;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/transaction")
public class ClientVNPayController {


    private final VNPayService vnPayService;

    @PostMapping("/checkout")
    public Object submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo
    ) {
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo);
        return ResponseHelper.getResponse(vnpayUrl, HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    public Object getApiSuccess(HttpServletRequest request) {
        Order order = vnPayService.authenticateVnPay(request);
        if (order != null) {
            return new RedirectView("https://stepupsneaker.pro/success/" + order.getId());
        }
        return new RedirectView("https://stepupsneaker.pro/pages/checkout");
    }

}
