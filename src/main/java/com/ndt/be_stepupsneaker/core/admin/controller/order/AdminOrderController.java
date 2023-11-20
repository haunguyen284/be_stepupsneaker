package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentMethodResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @Autowired
    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping("")
    public Object findAllOrder(AdminOrderRequest adminOrderRequest){
        PageableObject<AdminOrderResponse> listOrder = adminOrderService.findAllEntity(adminOrderRequest);

        return ResponseHelper.getResponse(listOrder, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminOrderResponse adminOrderResponse = adminOrderService.findById(id);

        return ResponseHelper.getResponse(adminOrderResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminOrderRequest adminOrderRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminOrderService.create(adminOrderRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminOrderRequest adminOrderRequest, BindingResult bindingResult){
        adminOrderRequest.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminOrderService.update(adminOrderRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminOrderService.delete(id), HttpStatus.OK);
    }

}
