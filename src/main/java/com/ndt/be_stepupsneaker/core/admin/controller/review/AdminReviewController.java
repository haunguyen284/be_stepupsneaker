package com.ndt.be_stepupsneaker.core.admin.controller.review;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.review.AdminReviewRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.review.AdminReviewResponse;
import com.ndt.be_stepupsneaker.core.admin.service.review.AdminReviewService;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.client.service.review.ClientReviewService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product/reviews")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping("")
    public Object findAllReview(AdminReviewRequest reviewRequest){
        PageableObject<AdminReviewResponse> reviews = adminReviewService.findAllEntity(reviewRequest);
        return ResponseHelper.getResponse(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminReviewResponse adminReviewResponse = adminReviewService.findById(id);
        return ResponseHelper.getResponse(adminReviewResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminReviewRequest reviewRequest, BindingResult bindingResult){
        reviewRequest.setId(id);
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(adminReviewService.update(reviewRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminReviewService.delete(id), HttpStatus.OK);
    }
}
