package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderDetailService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/order-details")
public class AdminOrderDetailController {

    private final AdminOrderDetailService adminOrderDetailService;

    @Autowired
    public AdminOrderDetailController(AdminOrderDetailService adminOrderDetailService) {
        this.adminOrderDetailService = adminOrderDetailService;
    }

    @GetMapping("")
    public Object findAllOrder(AdminOrderDetailRequest adminOrderDetailRequest){
        PageableObject<AdminOrderDetailResponse> listOrderDetail = adminOrderDetailService.findAllEntity(adminOrderDetailRequest);

        return ResponseHelper.getResponse(listOrderDetail, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminOrderDetailResponse adminOrderDetailResponse = adminOrderDetailService.findById(id);

        return ResponseHelper.getResponse(adminOrderDetailResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody List<@Valid AdminOrderDetailRequest> adminOrderDetailRequests, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminOrderDetailService.create(adminOrderDetailRequests), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminOrderDetailRequest adminOrderDetailRequest, BindingResult bindingResult){
        adminOrderDetailRequest.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminOrderDetailService.update(adminOrderDetailRequest), HttpStatus.OK);
    }

    @PutMapping("")
    public Object update(@RequestBody List<@Valid AdminOrderDetailRequest> adminOrderDetailRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminOrderDetailService.update(adminOrderDetailRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminOrderDetailService.delete(id), HttpStatus.OK);
    }

}
