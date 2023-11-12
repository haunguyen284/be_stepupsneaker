package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherHistoryService;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/voucher-histories")
public class AdminVoucherHistoryController {
    @Autowired
    private AdminVoucherHistoryService adminVoucherHistoryService;
    
    @GetMapping("")
    public Object findAllVoucherHistory(AdminVoucherHistoryRequest voucherRequest) {
        PageableObject<AdminVoucherHistoryResponse> listVoucherHistory = adminVoucherHistoryService.findAllEntity(voucherRequest);
        return ResponseHelper.getResponse(listVoucherHistory, HttpStatus.OK);
    }
}
