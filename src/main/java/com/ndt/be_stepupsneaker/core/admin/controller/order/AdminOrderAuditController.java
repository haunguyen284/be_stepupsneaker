package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderAuditService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.util.EntityComparator;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/admin/orders/revisions")
@RequiredArgsConstructor
public class AdminOrderAuditController {

    private final AdminOrderAuditService adminOrderAuditService;

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        List<AdminOrderResponse> adminOrderResponseList = adminOrderAuditService.getOrderRevisions(id);


        AdminOrderResponse entity1 = adminOrderResponseList.get(0);
        AdminOrderResponse entity2 = adminOrderResponseList.get(1);

        System.out.println(entity1.findChanges(entity2));

        return ResponseHelper.getResponse(adminOrderResponseList, HttpStatus.OK);
    }


}
