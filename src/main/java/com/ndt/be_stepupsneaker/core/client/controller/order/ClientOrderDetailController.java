package com.ndt.be_stepupsneaker.core.client.controller.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderDetailService;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/order-details")
public class ClientOrderDetailController {

    private final ClientOrderDetailService clientOrderDetailService;

    @Autowired
    public ClientOrderDetailController(ClientOrderDetailService clientOrderDetailService) {
        this.clientOrderDetailService = clientOrderDetailService;
    }

    @GetMapping("")
    public Object findAllOrder(ClientOrderDetailRequest ClientOrderDetailRequest){
        PageableObject<ClientOrderDetailResponse> listOrderDetail = clientOrderDetailService.findAllEntity(ClientOrderDetailRequest);

        return ResponseHelper.getResponse(listOrderDetail, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        ClientOrderDetailResponse ClientOrderDetailResponse = clientOrderDetailService.findById(id);

        return ResponseHelper.getResponse(ClientOrderDetailResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody List<@Valid ClientOrderDetailRequest> ClientOrderDetailRequests, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientOrderDetailService.create(ClientOrderDetailRequests), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid ClientOrderDetailRequest ClientOrderDetailRequest, BindingResult bindingResult){
        ClientOrderDetailRequest.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientOrderDetailService.update(ClientOrderDetailRequest), HttpStatus.OK);
    }

    @PutMapping("")
    public Object update(@RequestBody List<@Valid ClientOrderDetailRequest> ClientOrderDetailRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientOrderDetailService.update(ClientOrderDetailRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(clientOrderDetailService.delete(id), HttpStatus.OK);
    }

}
