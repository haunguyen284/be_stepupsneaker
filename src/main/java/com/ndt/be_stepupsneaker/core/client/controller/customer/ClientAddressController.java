package com.ndt.be_stepupsneaker.core.client.controller.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientAddressResponse;
import com.ndt.be_stepupsneaker.core.client.service.customer.ClientAddressService;
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

@RestController
@RequestMapping("/client/addresses")
public class ClientAddressController {

    private ClientAddressService clientAddressService;

    @Autowired
    public ClientAddressController(ClientAddressService clientAddressService) {
        this.clientAddressService = clientAddressService;
    }

    @GetMapping("")
    public Object findAllAddress(@RequestParam(name = "customer",required = false,defaultValue = "") String customerId, ClientAddressRequest addressDTO) {
        PageableObject<ClientAddressResponse> pageAddress = clientAddressService.findAllAddress(customerId, addressDTO);
        return ResponseHelper.getResponse(pageAddress, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientAddressRequest addressDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(clientAddressService.create(addressDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid ClientAddressRequest addressDTO, BindingResult bindingResult) {
        addressDTO.setId(id);
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(clientAddressService.update(addressDTO), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(clientAddressService.findById(id), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(clientAddressService.delete(id), HttpStatus.OK);
    }

    @PutMapping("/set-default-address")
    public Object setDefaultAddressByCustomer(
            @RequestParam("address") String addressId) {
        return ResponseHelper.getResponse(clientAddressService.updateDefaultAddressByCustomer(addressId), HttpStatus.OK);
    }

}
