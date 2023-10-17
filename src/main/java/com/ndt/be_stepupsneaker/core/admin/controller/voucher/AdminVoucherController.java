package com.ndt.be_stepupsneaker.core.admin.controller.voucher;


import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/vouchers")
public class AdminVoucherController {
    @Autowired
    private AdminVoucherService adminVoucherService;

    @GetMapping("")
    public Object findAllVoucher(AdminVoucherRequest voucherRequest) {
        PageableObject<AdminVoucherResponse> listVoucher = adminVoucherService.findAllEntity(voucherRequest);
        System.out.println(voucherRequest.getStatus());
        System.out.println("======="+voucherRequest.getStartDate());
        return ResponseHelper.getResponse(listVoucher, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        AdminVoucherResponse adminVoucherResponse = adminVoucherService.findById(UUID.fromString(id));
        return ResponseHelper.getResponse(adminVoucherResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminVoucherRequest voucherRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminVoucherService.create(voucherRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminVoucherRequest voucherRequest, BindingResult bindingResult) {
        voucherRequest.setId(UUID.fromString(id));
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminVoucherService.update(voucherRequest), HttpStatus.OK);
    }

    @PutMapping("delete/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminVoucherService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}
