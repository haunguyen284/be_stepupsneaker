package com.ndt.be_stepupsneaker.core.client.controller.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminCustomerService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client/customers")
public class ClientCustomerController {
    @Autowired
    private AdminCustomerService adminCustomerService;


    @GetMapping("/{id}")
    public Object findAllCustomerById(@PathVariable("id")String id){
        AdminCustomerResponse response = adminCustomerService.findById(id);
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
        customerDTO.setId(id);
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminCustomerService.update(customerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id){
        return ResponseHelper.getResponse(adminCustomerService.delete(id), HttpStatus.OK);
    }
}
