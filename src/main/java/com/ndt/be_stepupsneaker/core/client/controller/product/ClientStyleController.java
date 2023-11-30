package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientStyleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientStyleResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientStyleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client/products/styles")
public class ClientStyleController {
    @Autowired
    private ClientStyleService clientStyleService;

    @GetMapping("")
    public Object findAllStyle(ClientStyleRequest request){
        PageableObject<ClientStyleResponse> listStyle = clientStyleService.findAllEntity(request);

        return ResponseHelper.getResponse(listStyle, HttpStatus.OK);
    }
}