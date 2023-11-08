package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.ListCustomerIdAndVoucherIdRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Object create(@RequestBody @Valid ListCustomerIdAndVoucherIdRequest listCustomerIdAndVoucherIdRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminCustomerVoucherService.createCustomerVoucher(listCustomerIdAndVoucherIdRequest.getVoucher(), listCustomerIdAndVoucherIdRequest.getCustomer()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object deleteCustomersByVoucherAndCustomerIds(
            @PathVariable("id") String voucherId,
            @RequestBody ListCustomerIdAndVoucherIdRequest customerIdRequest) {
        UUID voucherUUID = UUID.fromString(voucherId);
        Boolean deleted = adminCustomerVoucherService.deleteCustomersByVoucherIdAndCustomerIds(voucherUUID, customerIdRequest.getCustomer());
        if (deleted) {
            return ResponseHelper.getResponse("CUSTOMERS DELETED SUCCESSFULLY.", HttpStatus.OK);
        } else {
            return ResponseHelper.getResponse("NO CUSTOMERS WERE DELETED", HttpStatus.NOT_FOUND);
        }
    }
}
