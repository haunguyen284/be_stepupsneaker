package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.CommonRequest;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionProductDetailService;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/promotion-product-details")
@RequiredArgsConstructor
public class AdminPromotionProductDetailController {
    private final AdminPromotionProductDetailService adminPromotionProductDetailService;
    private final MessageUtil messageUtil;

    @PostMapping("")
    public Object create(@RequestBody @Valid CommonRequest commonRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminPromotionProductDetailService.createPromotionProductDetail(commonRequest.getPromotion(), commonRequest.getProductDetails()), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public Object deleteProductDetailsByPromotionId(
            @PathVariable("id") String id,
            @RequestBody AdminPromotionProductDetailRequest request) {
        Boolean deleted = adminPromotionProductDetailService.deleteProductDetailsByPromotionId(id, request.getProductDetails());
        if (deleted) {
            return ResponseHelper.getResponse("ProductDetails " + messageUtil.getMessage("deleted.success"), HttpStatus.OK);
        } else {
            return ResponseHelper.getResponse(messageUtil.getMessage("delete.failed"), HttpStatus.NOT_FOUND);
        }
    }
}
