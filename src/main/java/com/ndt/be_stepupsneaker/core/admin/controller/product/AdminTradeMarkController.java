package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminTradeMarkResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminTradeMarkService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/trade-marks")
public class AdminTradeMarkController {
    @Autowired
    private AdminTradeMarkService adminTradeMarkService;

    @GetMapping("")
    public Object findAllTradeMark(AdminTradeMarkRequest colorDTO){
        PageableObject<AdminTradeMarkResponse> listTradeMark = adminTradeMarkService.findAllEntity(colorDTO);

        return ResponseHelper.getResponse(listTradeMark, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminTradeMarkResponse adminTradeMarkResponse = adminTradeMarkService.findById(UUID.fromString(id));

        return ResponseHelper.getResponse(adminTradeMarkResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminTradeMarkRequest colorDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminTradeMarkService.create(colorDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminTradeMarkRequest colorDTO, BindingResult bindingResult){
        colorDTO.setId(UUID.fromString(id));

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminTradeMarkService.update(colorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminTradeMarkService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}