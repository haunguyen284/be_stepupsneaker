package com.ndt.be_stepupsneaker.core.admin.controller.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminReturnFormHistoryService;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminReturnFormService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/return-histories")

public class AdminReturnFormHistoryController {
    @Autowired
    private AdminReturnFormHistoryService adminReturnFormHistoryService;

    @GetMapping("/{id}")
    public Object findAllByReturnFormId(@PathVariable("id") String id, AdminReturnFormRequest request){
        request.setId(id);
        PageableObject<AdminReturnFormHistoryResponse> listObj = adminReturnFormHistoryService.findAllEntity(request);
        return ResponseHelper.getResponse(listObj, HttpStatus.OK);
    }
}
