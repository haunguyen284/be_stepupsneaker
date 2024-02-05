package com.ndt.be_stepupsneaker.core.client.controller.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartDetailResponse;
import com.ndt.be_stepupsneaker.core.client.service.cart.ClientCartDetailService;
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

import java.util.List;

@RestController
@RequestMapping("/client/cart-details")
public class ClientCartDetailController {
    private final ClientCartDetailService clientCartDetailService;

    @Autowired
    public ClientCartDetailController(ClientCartDetailService clientCartDetailService) {
        this.clientCartDetailService = clientCartDetailService;
    }


    @GetMapping("")
    public Object findAllCartDetail() {
        return ResponseHelper.getResponse(clientCartDetailService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        ClientCartDetailResponse resp = clientCartDetailService.findById(id);
        return ResponseHelper.getResponse(resp, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientCartDetailRequest CartDetailDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        }
        return ResponseHelper.getResponse(clientCartDetailService.create(CartDetailDTO), HttpStatus.OK);
    }

    @PostMapping("/merge")
    public Object merge(
            @RequestBody List<@Valid ClientCartDetailRequest> cartDetailRequests,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(clientCartDetailService.merge(cartDetailRequests), HttpStatus.OK);
    }

    @PutMapping("/decrease/{id}")
    public Object decreaseQty(@PathVariable("id") String id, @RequestBody @Valid ClientCartDetailRequest CartDetailRequest, BindingResult bindingResult) {
        CartDetailRequest.setId(id);

        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(clientCartDetailService.decreaseQuantity(CartDetailRequest), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public Object updateQty(@PathVariable("id") String id, @RequestBody @Valid ClientCartDetailRequest CartDetailRequest, BindingResult bindingResult) {
        CartDetailRequest.setId(id);

        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(clientCartDetailService.updateQuantity(CartDetailRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(clientCartDetailService.deleteFromCart(id), HttpStatus.OK);
    }

    @DeleteMapping("")
    public Object deleteCartDetails() {
        return ResponseHelper.getResponse(clientCartDetailService.deleteAllFromCart(), HttpStatus.OK);
    }
}
