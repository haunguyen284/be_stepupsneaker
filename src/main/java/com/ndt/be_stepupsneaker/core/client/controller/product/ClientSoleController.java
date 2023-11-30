package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSoleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSoleResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientSoleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client/products/soles")
public class ClientSoleController {
    @Autowired
    private ClientSoleService clientSoleService;

    @GetMapping("")
    public Object findAllSole(ClientSoleRequest request){
        PageableObject<ClientSoleResponse> listSole = clientSoleService.findAllEntity(request);

        return ResponseHelper.getResponse(listSole, HttpStatus.OK);
    }
}
