package com.ndt.be_stepupsneaker.core.client.controller.review;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientBrandRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSoleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientBrandResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSoleResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientBrandService;
import com.ndt.be_stepupsneaker.core.client.service.review.ClientReviewService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/product/reviews")
public class ClientReviewController {

    private final ClientReviewService clientReviewService;


    @GetMapping("")
    public Object findAllReview(ClientReviewRequest request){
        PageableObject<ClientReviewResponse> listSole = clientReviewService.findAllEntity(request);
        return ResponseHelper.getResponse(listSole, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        ClientReviewResponse clientReviewResponse = clientReviewService.findById(id);

        return ResponseHelper.getResponse(clientReviewResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid ClientReviewRequest clientReviewRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(clientReviewService.create(clientReviewRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid ClientReviewRequest clientReviewRequest, BindingResult bindingResult){
        clientReviewRequest.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(clientReviewService.update(clientReviewRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(clientReviewService.delete(id), HttpStatus.OK);
    }
}
