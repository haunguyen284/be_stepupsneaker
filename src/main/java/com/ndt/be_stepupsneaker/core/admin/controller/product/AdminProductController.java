package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.cloudinary.Cloudinary;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    private AdminProductService adminProductService;

    @GetMapping("")
    public Object findAllProduct(AdminProductRequest colorDTO){
        PageableObject<AdminProductResponse> listProduct = adminProductService.findAllEntity(colorDTO);
        return ResponseHelper.getResponse(listProduct, HttpStatus.OK);
    }

    @GetMapping("/status")
    public Object findAllStatusProduct(){
        List<ProductPropertiesStatus> propertiesStatusList = Arrays.asList(ProductPropertiesStatus.values());
        return ResponseHelper.getResponse(propertiesStatusList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminProductResponse adminProductResponse = adminProductService.findById(id);

        return ResponseHelper.getResponse(adminProductResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminProductRequest colorDTO, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductService.create(colorDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminProductRequest colorDTO, BindingResult bindingResult){
        colorDTO.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminProductService.update(colorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminProductService.delete(id), HttpStatus.OK);
    }
}
