package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
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

import java.util.UUID;

@RestController
@RequestMapping("/admin/order-histories")
public class AdminOrderHistoryController {

    private final AdminOrderHistoryService adminOrderHistoryService;

    @Autowired
    public AdminOrderHistoryController(AdminOrderHistoryService adminOrderHistoryService) {
        this.adminOrderHistoryService = adminOrderHistoryService;
    }

    @GetMapping("")
    public Object findAllOrderHistory(AdminOrderHistoryRequest adminOrderHistoryRequest){
        PageableObject<AdminOrderHistoryResponse> listOrderHistory = adminOrderHistoryService.findAllEntity(adminOrderHistoryRequest);

        return ResponseHelper.getResponse(listOrderHistory, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminOrderHistoryResponse adminOrderHistoryResponse = adminOrderHistoryService.findById(id);

        return ResponseHelper.getResponse(adminOrderHistoryResponse, HttpStatus.OK);
    }

}
