package com.ndt.be_stepupsneaker.core.client.controller.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientCustomerMapper;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/orders")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;
    private final MySessionInfo mySessionInfo;

    @Autowired
    public ClientOrderController(ClientOrderService clientOrderService, MySessionInfo mySessionInfo) {
        this.clientOrderService = clientOrderService;
        this.mySessionInfo = mySessionInfo;
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        ClientCustomerResponse clientCustomerResponse = mySessionInfo.getCurrentCustomer();
        String customerId = clientCustomerResponse != null ? clientCustomerResponse.getId() : null;
        ClientOrderResponse clientOrderResponse = clientOrderService.findByIdAndCustomerId(id, customerId);
        return ResponseHelper.getResponse(clientOrderResponse, HttpStatus.OK);
    }

    @GetMapping("/tracking/{code}")
    public Object findByCode(@PathVariable("code") String code) {
        ClientOrderResponse clientOrderResponse = clientOrderService.findByCode(code);
        return ResponseHelper.getResponse(clientOrderResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientOrderRequest ClientOrderRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientOrderService.create(ClientOrderRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid ClientOrderRequest ClientOrderRequest, BindingResult bindingResult) {
        ClientOrderRequest.setId(id);
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(clientOrderService.update(ClientOrderRequest), HttpStatus.OK);
    }


}
