package com.ndt.be_stepupsneaker.core.client.controller.order;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/orders")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @Autowired
    public ClientOrderController(ClientOrderService clientOrderService) {
        this.clientOrderService = clientOrderService;
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        ClientOrderResponse ClientOrderResponse = clientOrderService.findById(id);

        return ResponseHelper.getResponse(ClientOrderResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientOrderRequest ClientOrderRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientOrderService.create(ClientOrderRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid ClientOrderRequest ClientOrderRequest, BindingResult bindingResult){
        ClientOrderRequest.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientOrderService.update(ClientOrderRequest), HttpStatus.OK);
    }


}
