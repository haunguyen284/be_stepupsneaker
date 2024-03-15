package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductDetailService;
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
        AdminProductDetailResponse adminProductDetailResponse = adminProductDetailService.findById(id);

        return ResponseHelper.getResponse(adminProductDetailResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody List<@Valid AdminProductDetailRequest> productDetailRequests, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductDetailService.create(productDetailRequests), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminProductDetailRequest colorDTO, BindingResult bindingResult){
        colorDTO.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductDetailService.update(colorDTO), HttpStatus.OK);
    }

    @PutMapping("")
    public Object update(@RequestBody List<@Valid AdminProductDetailRequest> productDetailRequests, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductDetailService.update(productDetailRequests), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminProductDetailService.delete(id), HttpStatus.OK);
    }

    @GetMapping("/trending")
    public Object trending(@RequestParam("start")Long fromDate, @RequestParam("end")Long toDate) {

        return ResponseHelper.getResponse((adminProductDetailService.findByTrending(fromDate, toDate)), HttpStatus.OK);
    }
}