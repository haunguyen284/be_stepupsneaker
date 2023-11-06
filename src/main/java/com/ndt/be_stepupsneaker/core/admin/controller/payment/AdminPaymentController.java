package com.ndt.be_stepupsneaker.core.admin.controller.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentResponse;
import com.ndt.be_stepupsneaker.core.admin.service.payment.AdminPaymentService;
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
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @Autowired
    public AdminPaymentController(AdminPaymentService adminPaymentService) {
        this.adminPaymentService = adminPaymentService;
    }

    @GetMapping("")
    public Object findAllColor(AdminPaymentRequest adminPaymentRequest){
        PageableObject<AdminPaymentResponse> listPayment = adminPaymentService.findAllEntity(adminPaymentRequest);

        return ResponseHelper.getResponse(listPayment, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminPaymentResponse adminPaymentResponse = adminPaymentService.findById(UUID.fromString(id));

        return ResponseHelper.getResponse(adminPaymentResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminPaymentRequest adminPaymentRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminPaymentService.create(adminPaymentRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminPaymentRequest adminPaymentRequest, BindingResult bindingResult){
        adminPaymentRequest.setId(UUID.fromString(id));

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminPaymentService.update(adminPaymentRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminPaymentService.delete(UUID.fromString(id)), HttpStatus.OK);
    }

}
