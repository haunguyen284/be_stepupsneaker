package com.ndt.be_stepupsneaker.core.client.controller.voucher;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.service.voucher.ClientVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/vouchers")
public class ClientVoucherController {
    @Autowired
    private ClientVoucherService clientVoucherService;


    @GetMapping("")
    public Object findAllVoucher(ClientVoucherRequest voucherRequest,
                                 @RequestParam(value = "customer", required = false, defaultValue = "") String customerId,
                                 @RequestParam(value = "noCustomer", required = false, defaultValue = "") String noCustomerId) {
        PageableObject<ClientVoucherResponse> listVoucher = clientVoucherService.findAllVoucher(voucherRequest, customerId, noCustomerId);
        return ResponseHelper.getResponse(listVoucher, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        ClientVoucherResponse ClientVoucherResponse = clientVoucherService.findById(id);
        return ResponseHelper.getResponse(ClientVoucherResponse, HttpStatus.OK);
    }

}
