package com.ndt.be_stepupsneaker.core.admin.controller.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminCustomerService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {
    @Autowired
    private AdminCustomerService adminCustomerService;

    @GetMapping("")
    public Object findAllCustomer(AdminCustomerRequest customerDTO,
                                  @RequestParam(name = "voucher",required = false,defaultValue = "") UUID voucherId,
                                  @RequestParam(name = "noVoucher",required = false,defaultValue = "") UUID noVoucherId){
        PageableObject<AdminCustomerResponse> listCustomer = adminCustomerService.findAllCustomer(customerDTO,voucherId,noVoucherId);
        return ResponseHelper.getResponse(listCustomer, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findAllCustomerById(@PathVariable("id")String id){
        AdminCustomerResponse response = adminCustomerService.findById(UUID.fromString(id));
        return ResponseHelper.getResponse(response, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminCustomerRequest customerDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminCustomerService.create(customerDTO), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminCustomerRequest customerDTO, BindingResult bindingResult ){
        customerDTO.setId(UUID.fromString(id));
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminCustomerService.update(customerDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id){
        return ResponseHelper.getResponse(adminCustomerService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}
