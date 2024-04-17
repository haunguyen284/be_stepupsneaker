package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientProductDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/product-details")
public class ClientProductDetailController {
    @Autowired
    private ClientProductDetailService clientProductDetailService;

    @GetMapping("")
    public Object findAllProductDetail(ClientProductDetailRequest request){
        PageableObject<ClientProductDetailResponse> listProductDetail = clientProductDetailService.findAllEntity(request);
        return ResponseHelper.getResponse(listProductDetail, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        ClientProductDetailResponse adminProductDetailResponse = clientProductDetailService.findById(id);

        return ResponseHelper.getResponse(adminProductDetailResponse, HttpStatus.OK);
    }
}