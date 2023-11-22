package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientBrandRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientBrandResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientBrandService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/brands")
public class ClientBrandController {
    @Autowired
    private ClientBrandService clientBrandService;

    @GetMapping("")
    public Object findAllBrand(ClientBrandRequest request){
        PageableObject<ClientBrandResponse> listBrand = clientBrandService.findAllEntity(request);
        return ResponseHelper.getResponse(listBrand, HttpStatus.OK);
    }
}
