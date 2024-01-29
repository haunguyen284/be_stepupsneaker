package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.AdminOrderAuditResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order_audit.AdminOrderAuditService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/orders/revisions")
@RequiredArgsConstructor
public class AdminOrderAuditController {

    private final AdminOrderAuditService adminOrderAuditService;

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        List<AdminOrderAuditResponse> adminOrderResponseList = adminOrderAuditService.getOrderRevisions(id);


//        AdminOrderResponse entity1 = adminOrderResponseList.get(0);
//        AdminOrderResponse entity2 = adminOrderResponseList.get(1);

//        System.out.println(entity1.findChanges(entity2));

        return ResponseHelper.getResponse(adminOrderResponseList, HttpStatus.OK);
    }


}
