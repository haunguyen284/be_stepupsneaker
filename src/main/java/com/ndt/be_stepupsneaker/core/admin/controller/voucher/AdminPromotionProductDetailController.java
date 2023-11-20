package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionProductDetailService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/promotion-product-details")
public class AdminPromotionProductDetailController {
    private AdminPromotionProductDetailService adminPromotionProductDetailService;

    @Autowired
    public AdminPromotionProductDetailController(AdminPromotionProductDetailService adminPromotionProductDetailService) {
        this.adminPromotionProductDetailService = adminPromotionProductDetailService;
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminPromotionProductDetailRequest adminPromotionProductDetailRequest,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminPromotionProductDetailService.create(adminPromotionProductDetailRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object deleteProductDetailsByPromotionId(
            @PathVariable("id") String id,
            @RequestBody AdminPromotionProductDetailRequest request) {
        Boolean deleted = adminPromotionProductDetailService.deleteProductDetailsByPromotionId(id, request.getProductDetails());
        if (deleted) {
            return ResponseHelper.getResponse("ProductDetails DELETED SUCCESSFULLY.", HttpStatus.OK);
        } else {
            return ResponseHelper.getResponse("NO ProductDetails WERE DELETED", HttpStatus.NOT_FOUND);
        }
    }
}
