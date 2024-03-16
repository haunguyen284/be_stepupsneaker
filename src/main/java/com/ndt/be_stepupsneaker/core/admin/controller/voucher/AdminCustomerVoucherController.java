package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.CommonRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminCustomerVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/customerVoucher")
@RequiredArgsConstructor
public class AdminCustomerVoucherController {

    private final AdminCustomerVoucherService adminCustomerVoucherService;
    private final MessageUtil messageUtil;

    // not use this function
    @GetMapping("")
    public Object findAllCustomerVoucher(AdminCustomerVoucherRequest customerVoucherReq) {
        PageableObject<AdminCustomerVoucherResponse> page = adminCustomerVoucherService.findAllEntity(customerVoucherReq);
        return ResponseHelper.getResponse(page, HttpStatus.OK);
    }

    // not use this function
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        AdminCustomerVoucherResponse customerVoucherResponse = adminCustomerVoucherService.findById(id);
        return ResponseHelper.getResponse(customerVoucherResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid CommonRequest commonRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminCustomerVoucherService.createCustomerVoucher(commonRequest.getVoucher(), commonRequest.getCustomers()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object deleteCustomersByVoucherAndCustomerIds(
            @PathVariable("id") String voucherId,
            @RequestBody CommonRequest customerIdRequest) {
        Boolean deleted = adminCustomerVoucherService.deleteCustomersByVoucherIdAndCustomerIds(voucherId, customerIdRequest.getCustomers());
        if (deleted) {
            return ResponseHelper.getResponse("customer " + messageUtil.getMessage("deleted.success"), HttpStatus.OK);
        } else {
            return ResponseHelper.getResponse(messageUtil.getMessage("delete.failed"), HttpStatus.NOT_FOUND);
        }
    }
}
