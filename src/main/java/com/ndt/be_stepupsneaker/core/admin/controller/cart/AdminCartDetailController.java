package com.ndt.be_stepupsneaker.core.admin.controller.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.service.cart.AdminCartDetailService;
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
@RequestMapping("/admin/cart-details")
public class AdminCartDetailController {
    @Autowired
    private AdminCartDetailService adminCartDetailService;

    @GetMapping("")
    public Object findAllCartDetail(AdminCartDetailRequest CartDetailDTO){
        PageableObject<AdminCartDetailResponse> listCartDetail = adminCartDetailService.findAllEntity(CartDetailDTO);
        return ResponseHelper.getResponse(listCartDetail, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminCartDetailResponse resp = adminCartDetailService.findById(UUID.fromString(id));
        return ResponseHelper.getResponse(resp, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminCartDetailRequest CartDetailDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);

        }
        return ResponseHelper.getResponse(adminCartDetailService.create(CartDetailDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminCartDetailRequest CartDetailRequest, BindingResult bindingResult){
        CartDetailRequest.setId(UUID.fromString(id));
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminCartDetailService.update(CartDetailRequest), HttpStatus.OK);

    }

    @DeleteMapping("")
    public Object delete(@RequestBody AdminCartDetailRequest request){
        return ResponseHelper.getResponse(adminCartDetailService.deleteCartDetails(request), HttpStatus.OK);
    }
}
