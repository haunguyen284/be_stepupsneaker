package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSizeRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSizeResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientSizeService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/sizes")
public class ClientSizeController {
    @Autowired
    private ClientSizeService clientSizeService;

    @GetMapping("")
    public Object findAllSize(ClientSizeRequest request){
        PageableObject<ClientSizeResponse> listSize = clientSizeService.findAllEntity(request);
        return ResponseHelper.getResponse(listSize, HttpStatus.OK);
    }
}
