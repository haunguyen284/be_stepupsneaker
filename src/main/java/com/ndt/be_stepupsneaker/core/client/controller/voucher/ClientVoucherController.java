package com.ndt.be_stepupsneaker.core.client.controller.voucher;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.service.voucher.ClientVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/vouchers")
public class ClientVoucherController {
    @Autowired
    private ClientVoucherService clientVoucherService;

    @Autowired
    private MySessionInfo mySessionInfo;


    @GetMapping("")
    public Object findAllVoucher(ClientVoucherRequest voucherRequest,
                                 @RequestParam(value = "customer", required = false, defaultValue = "") String customerId,
                                 @RequestParam(value = "noCustomer", required = false, defaultValue = "") String noCustomerId) {
        ClientCustomerResponse response = mySessionInfo.getCurrentCustomer();
        PageableObject<ClientVoucherResponse> listVoucher = clientVoucherService.findAllVoucher(voucherRequest, response.getId());
        return ResponseHelper.getResponse(listVoucher, HttpStatus.OK);
    }

    @GetMapping("/get-legit-voucher")
    public Object findLegitVoucher(@RequestParam(value = "totalMoney") float totalMoney){
        ClientCustomerResponse response = mySessionInfo.getCurrentCustomer();
        PageableObject<ClientVoucherResponse> legitVouchers = clientVoucherService.findLegitVouchers(response.getId(), totalMoney);
        return ResponseHelper.getResponse(legitVouchers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        ClientVoucherResponse ClientVoucherResponse = clientVoucherService.findById(id);
        return ResponseHelper.getResponse(ClientVoucherResponse, HttpStatus.OK);
    }

}
