package com.ndt.be_stepupsneaker.core.client.controller.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientReturnFormRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientReturnFormResponse;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientReturnFormService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/return-forms")
public class ClientReturnFormController {
    @Autowired
    private ClientReturnFormService clientReturnFormService;

    @GetMapping("")
    public Object findAll(ClientReturnFormRequest request){
        PageableObject<ClientReturnFormResponse> listObj = clientReturnFormService.findAllEntity(request);

        return ResponseHelper.getResponse(listObj, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientReturnFormRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientReturnFormService.create(request), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        ClientReturnFormResponse response = clientReturnFormService.findById(id);

        return ResponseHelper.getResponse(response, HttpStatus.OK);
    }

    @GetMapping("/tracking/{code}")
    public Object findByCode(@PathVariable("code") String code) {
        ClientReturnFormResponse response = clientReturnFormService.findByCode(code);

        return ResponseHelper.getResponse(response, HttpStatus.OK);
    }
}
