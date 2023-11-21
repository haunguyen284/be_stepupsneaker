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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/cart-details")
public class ClientCartDetailController {
    @Autowired
    private ClientCartDetailService clientCartDetailService;

    @GetMapping("")
    public Object findAllCartDetail(ClientCartDetailRequest CartDetailDTO) {
        PageableObject<ClientCartDetailResponse> listCartDetail = clientCartDetailService.findAllEntity(CartDetailDTO);
        return ResponseHelper.getResponse(listCartDetail, HttpStatus.OK);

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

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid ClientCartDetailRequest CartDetailRequest, BindingResult bindingResult) {
        CartDetailRequest.setId(id);
        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(clientCartDetailService.update(CartDetailRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(clientCartDetailService.delete(id), HttpStatus.OK);
    }

    @DeleteMapping("")
    public Object deleteCartDetails(@RequestBody ClientCartDetailRequest request) {
        return ResponseHelper.getResponse(clientCartDetailService.deleteCartDetails(request), HttpStatus.OK);
    }
}
