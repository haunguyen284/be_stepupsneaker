package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductService;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientProductService;
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

@RestController
@RequestMapping("/client/products")
public class ClientProductController {
    @Autowired
    private ClientProductService clientProductService;

    @GetMapping("")
    public Object findAllProduct(ClientProductRequest request){
        PageableObject<ClientProductResponse> listProduct = clientProductService.findAllEntity(request);
        return ResponseHelper.getResponse(listProduct, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        ClientProductResponse clientProductResponse = clientProductService.findById(id);

        return ResponseHelper.getResponse(clientProductResponse, HttpStatus.OK);
    }

}
