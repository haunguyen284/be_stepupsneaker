package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientColorRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientColorResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientColorService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/colors")
public class ClientColorController {
    @Autowired
    private ClientColorService clientColorService;

    @GetMapping("")
    public Object findAllColor(ClientColorRequest request){
        PageableObject<ClientColorResponse> listColor = clientColorService.findAllEntity(request);

        return ResponseHelper.getResponse(listColor, HttpStatus.OK);
    }

}
