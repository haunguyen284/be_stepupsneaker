package com.ndt.be_stepupsneaker.core.admin.controller.voucher;


import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.CustomerIdRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/vouchers")
public class AdminVoucherController {
    @Autowired
    private AdminVoucherService adminVoucherService;
    @Autowired
    private AdminCustomerVoucherService adminCustomerVoucherService;

    @GetMapping("")
    public Object findAllVoucher(AdminVoucherRequest voucherRequest) {
        PageableObject<AdminVoucherResponse> listVoucher = adminVoucherService.findAllEntity(voucherRequest);
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

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminVoucherService.delete(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping("/getAllCustomerByVoucherId/{voucherId}/search")
    public Object findAllCustomerVoucherByVoucherId(@PathVariable("voucherId") String voucherId, AdminCustomerRequest customerRequest) {
        PageableObject<AdminCustomerResponse> customerList = adminCustomerVoucherService.getAllCustomerByVoucherId(UUID.fromString(voucherId), customerRequest);
        return ResponseHelper.getResponse(customerList, HttpStatus.OK);
    }

    @GetMapping("/getAllCustomerNotInVoucherId/{voucherId}/search")
    public Object findAllCustomerVoucherNotInVoucherId(@PathVariable("voucherId") String voucherId, AdminCustomerRequest customerRequest) {
        PageableObject<AdminCustomerResponse> customerList = adminCustomerVoucherService
                .getAllCustomerNotInVoucherId(UUID.fromString(voucherId), customerRequest);
        return ResponseHelper.getResponse(customerList, HttpStatus.OK);
    }



}
