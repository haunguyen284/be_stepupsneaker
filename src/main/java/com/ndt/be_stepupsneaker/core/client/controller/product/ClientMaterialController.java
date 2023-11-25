package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientMaterialRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientMaterialResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientMaterialService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client/materials")
public class ClientMaterialController {
    @Autowired
    private ClientMaterialService clientMaterialService;

    @GetMapping("")
    public Object findAllMaterial(ClientMaterialRequest request){
        PageableObject<ClientMaterialResponse> listMaterial = clientMaterialService.findAllEntity(request);

        return ResponseHelper.getResponse(listMaterial, HttpStatus.OK);
    }
}