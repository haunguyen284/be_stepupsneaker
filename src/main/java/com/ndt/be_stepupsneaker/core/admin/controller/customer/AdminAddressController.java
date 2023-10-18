package com.ndt.be_stepupsneaker.core.admin.controller.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminAddressService;
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
@RequestMapping("/admin/address")
public class AdminAddressController {

    @Autowired
    private AdminAddressService adminAddressService;

    @GetMapping("")
    public Object findAllAddress(AdminAddressRequest addressDTO){
        PageableObject<AdminAddressResponse> listAddress = adminAddressService.findAllEntity(addressDTO);
        return ResponseHelper.getResponse(listAddress, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminAddressRequest addressDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminAddressService.create(addressDTO), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id,  @RequestBody @Valid AdminAddressRequest addressDTO, BindingResult bindingResult){
        addressDTO.setId(UUID.fromString(id));

        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminAddressService.update(addressDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id){
        return ResponseHelper.getResponse(adminAddressService.delete(UUID.fromString(id)), HttpStatus.OK);
    }

}
