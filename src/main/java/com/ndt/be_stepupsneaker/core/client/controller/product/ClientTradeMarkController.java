package com.ndt.be_stepupsneaker.core.client.controller.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientTradeMarkResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientTradeMarkService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/products/trade-marks")
public class ClientTradeMarkController {
    @Autowired
    private ClientTradeMarkService clientTradeMarkService;

    @GetMapping("")
    public Object findAllTradeMark(ClientTradeMarkRequest request){
        PageableObject<ClientTradeMarkResponse> listTradeMark = clientTradeMarkService.findAllEntity(request);

        return ResponseHelper.getResponse(listTradeMark, HttpStatus.OK);
    }
}