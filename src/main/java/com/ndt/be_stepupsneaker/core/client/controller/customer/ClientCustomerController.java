package com.ndt.be_stepupsneaker.core.client.controller.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.service.customer.ClientCustomerService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.ChangePasswordRequest;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.service.AuthenticationService;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client/customers")
@RequiredArgsConstructor
public class ClientCustomerController {
    private final ClientCustomerService clientCustomerService;
    private final MySessionInfo mySessionInfo;
    private final AuthenticationService authenticationService;


    @GetMapping("/{id}")
    public Object findAllCustomerById(@PathVariable("id") String id) {
        ClientCustomerResponse response = clientCustomerService.findById(id);
        return ResponseHelper.getResponse(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public Object getMe() {
        return ResponseHelper.getResponse(mySessionInfo.getCurrentCustomer(), HttpStatus.OK);
    }


    @PutMapping("/change-password")
    public Object changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return ResponseHelper.getResponse(authenticationService.changePassword(request), HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientCustomerRequest customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(clientCustomerService.create(customerDTO), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid ClientCustomerRequest customerDTO, BindingResult bindingResult) {
        customerDTO.setId(id);
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(clientCustomerService.update(customerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(clientCustomerService.delete(id), HttpStatus.OK);
    }
}
