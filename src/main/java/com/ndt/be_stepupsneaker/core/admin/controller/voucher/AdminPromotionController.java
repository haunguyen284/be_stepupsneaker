package com.ndt.be_stepupsneaker.core.admin.controller.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionResponse;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/promotions")
public class AdminPromotionController {
    @Autowired
    private AdminPromotionService adminPromotionService;

    @GetMapping("")
    public Object findAllPromotion(AdminPromotionRequest PromotionRequest,
                                   @RequestParam(value = "productDetail", required = false, defaultValue = "") String productDetail,
                                   @RequestParam(value = "noProductDetail", required = false, defaultValue = "") String noProductDetail) {
        PageableObject<AdminPromotionResponse> listPromotion = adminPromotionService.findAllPromotion(PromotionRequest, productDetail, noProductDetail);
        return ResponseHelper.getResponse(listPromotion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        AdminPromotionResponse adminPromotionResponse = adminPromotionService.findById(id);
        return ResponseHelper.getResponse(adminPromotionResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminPromotionRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminPromotionService.create(request), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminPromotionRequest request, BindingResult bindingResult) {
        request.setId(id);
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminPromotionService.update(request), HttpStatus.OK);
    }

    @PutMapping("/deactivate-discount/{id}")
    public Object deactivateDiscount(@PathVariable("id") String id) {
        AdminPromotionResponse adminPromotionResponse = adminPromotionService.deactivateDiscount(id);
        return ResponseHelper.getResponse(adminPromotionResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        return ResponseHelper.getResponse(adminPromotionService.delete(id), HttpStatus.OK);
    }
}
