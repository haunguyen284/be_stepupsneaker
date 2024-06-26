package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminReturnFormService;
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

@RestController
@RequestMapping("/admin/return-forms")
public class AdminReturnFormController {
    @Autowired
    private AdminReturnFormService adminReturnFormService;

    @GetMapping("")
    public Object findAll(AdminReturnFormRequest request){
        PageableObject<AdminReturnFormResponse> listObj = adminReturnFormService.findAllEntity(request);

        return ResponseHelper.getResponse(listObj, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminReturnFormRequest request, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminReturnFormService.create(request), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminReturnFormRequest request, BindingResult bindingResult){
        request.setId(id);
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminReturnFormService.update(request), HttpStatus.OK);
    }

    @PutMapping("/update-return-delivery-status/{id}")
    public Object updateReturnDeliveryStatus(@PathVariable("id") String id, @RequestBody AdminReturnFormRequest request, BindingResult bindingResult){
        request.setId(id);
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminReturnFormService.updateReturnDeliveryStatus(request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        AdminReturnFormResponse response = adminReturnFormService.findById(id);

        return ResponseHelper.getResponse(response, HttpStatus.OK);
    }

    @GetMapping("/tracking/{order_code}")
    public Object findOrderByOrderCode(@PathVariable("order_code") String code) {
        AdminOrderResponse response = adminReturnFormService.findByCode(code);
        return ResponseHelper.getResponse(response, HttpStatus.OK);
    }

}
