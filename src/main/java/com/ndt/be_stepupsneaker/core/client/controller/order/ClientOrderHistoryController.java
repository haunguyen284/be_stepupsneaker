package com.ndt.be_stepupsneaker.core.client.controller.order;
import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/order-histories")
public class ClientOrderHistoryController {

    private final ClientOrderHistoryService clientOrderHistoryService;

    @Autowired
    public ClientOrderHistoryController(ClientOrderHistoryService clientOrderHistoryService) {
        this.clientOrderHistoryService = clientOrderHistoryService;
    }

    @GetMapping("")
    public Object findAllOrderHistory(ClientOrderHistoryRequest request){
        PageableObject<ClientOrderHistoryResponse> listOrderHistory = clientOrderHistoryService.findAllEntity(request);
        return ResponseHelper.getResponse(listOrderHistory, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        ClientOrderHistoryResponse ClientOrderHistoryResponse = clientOrderHistoryService.findById(id);
        return ResponseHelper.getResponse(ClientOrderHistoryResponse, HttpStatus.OK);
    }

}
