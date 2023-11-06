package com.ndt.be_stepupsneaker.core.admin.controller.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminAddressService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/addresses")
public class AdminAddressController {

    @Autowired
    private AdminAddressService adminAddressService;

    // Not user function
    @GetMapping("/get-all")
    public Object findAllCustomerAddress(AdminAddressRequest addressDTO) {
        PageableObject<AdminAddressResponse> listAddress = adminAddressService.findAllEntity(addressDTO);
        return ResponseHelper.getResponse(listAddress, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminAddressRequest addressDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminAddressService.create(addressDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminAddressRequest addressDTO, BindingResult bindingResult) {
        addressDTO.setId(UUID.fromString(id));
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminAddressService.update(addressDTO), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminAddressService.findById(UUID.fromString(id)), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminAddressService.delete(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping("")
    public Object findAllAddressByCustomerId(@RequestParam(name = "customerId",required = false,defaultValue = "") UUID customerId, AdminAddressRequest addressDTO) {
        PageableObject<AdminAddressResponse> pageAddress = adminAddressService.findAllAddress(customerId, addressDTO);
        return ResponseHelper.getResponse(pageAddress, HttpStatus.OK);
    }

    @PutMapping("/set-default-address")
    public Object setDefaultAddressByCustomer(
            @RequestParam("addressId") String addressId) {
        return ResponseHelper.getResponse(adminAddressService.updateDefaultAddressByCustomer(UUID.fromString(addressId)), HttpStatus.OK);
    }

}
