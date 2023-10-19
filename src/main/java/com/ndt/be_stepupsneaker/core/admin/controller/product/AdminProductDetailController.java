package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductDetailService;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/product-details")
public class AdminProductDetailController {
    @Autowired
    private AdminProductDetailService adminProductDetailService;

    @GetMapping("")
    public Object findAllProductDetail(AdminProductDetailRequest colorDTO){
        PageableObject<AdminProductDetailResponse> listProductDetail = adminProductDetailService.findAllEntity(colorDTO);
        return ResponseHelper.getResponse(listProductDetail, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminProductDetailResponse adminProductDetailResponse = adminProductDetailService.findById(UUID.fromString(id));

        return ResponseHelper.getResponse(adminProductDetailResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminProductDetailRequest colorDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductDetailService.create(colorDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminProductDetailRequest colorDTO, BindingResult bindingResult){
        colorDTO.setId(UUID.fromString(id));

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductDetailService.update(colorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminProductDetailService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}