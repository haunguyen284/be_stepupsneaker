package com.ndt.be_stepupsneaker.core.admin.controller.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentMethodRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentMethodResponse;
import com.ndt.be_stepupsneaker.core.admin.service.payment.AdminPaymentMethodService;
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
@RequestMapping("/admin/payment-methods")
public class AdminPaymentMethodController {

    private final AdminPaymentMethodService adminPaymentMethodService;

    @Autowired
    public AdminPaymentMethodController(AdminPaymentMethodService adminPaymentMethodService) {
        this.adminPaymentMethodService = adminPaymentMethodService;
    }

    @GetMapping("")
    public Object findAllPaymentMethod(AdminPaymentMethodRequest adminPaymentMethodRequest){
        PageableObject<AdminPaymentMethodResponse> listPaymentMethod = adminPaymentMethodService.findAllEntity(adminPaymentMethodRequest);

        return ResponseHelper.getResponse(listPaymentMethod, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id){
        AdminPaymentMethodResponse adminPaymentMethodResponse = adminPaymentMethodService.findById(id);

        return ResponseHelper.getResponse(adminPaymentMethodResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminPaymentMethodRequest adminPaymentMethodRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminPaymentMethodService.create(adminPaymentMethodRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminPaymentMethodRequest adminPaymentMethodRequest, BindingResult bindingResult){
        adminPaymentMethodRequest.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminPaymentMethodService.update(adminPaymentMethodRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminPaymentMethodService.delete(id), HttpStatus.OK);
    }

}
