package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.ListCustomerAndVoucher;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminSizeRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/customerVoucher")
public class AdminCustomerVoucherController {

    private AdminCustomerVoucherService adminCustomerVoucherService;

    @Autowired
    public AdminCustomerVoucherController(AdminCustomerVoucherService adminCustomerVoucherService) {
        this.adminCustomerVoucherService = adminCustomerVoucherService;
    }

    // not use this function
    @GetMapping("")
    public Object findAllCustomerVoucher(AdminCustomerVoucherRequest customerVoucherReq) {
        PageableObject<AdminCustomerVoucherResponse> page = adminCustomerVoucherService.findAllEntity(customerVoucherReq);
        return ResponseHelper.getResponse(page, HttpStatus.OK);
    }

    // not use this function
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        AdminCustomerVoucherResponse customerVoucherResponse = adminCustomerVoucherService.findById(UUID.fromString(id));
        return ResponseHelper.getResponse(customerVoucherResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ListCustomerAndVoucher listCustomerAndVoucher, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminCustomerVoucherService.createCustomerVoucher(listCustomerAndVoucher.getVoucherRequestList(), listCustomerAndVoucher.getCustomerRequestList()), HttpStatus.OK);
    }
}