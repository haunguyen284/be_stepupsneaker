package com.ndt.be_stepupsneaker.core.client.controller.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminAddressService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/addresses")
public class ClientAddressController {

    private AdminAddressService adminAddressService;

    @Autowired
    public ClientAddressController(AdminAddressService adminAddressService) {
        this.adminAddressService = adminAddressService;
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
        addressDTO.setId(id);
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminAddressService.update(addressDTO), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminAddressService.findById(id), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminAddressService.delete(id), HttpStatus.OK);
    }

    @PutMapping("/set-default-address")
    public Object setDefaultAddressByCustomer(
            @RequestParam("address") String addressId) {
        return ResponseHelper.getResponse(adminAddressService.updateDefaultAddressByCustomer(addressId), HttpStatus.OK);
    }

}
