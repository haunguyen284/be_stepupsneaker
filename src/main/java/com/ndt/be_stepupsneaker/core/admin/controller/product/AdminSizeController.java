package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSizeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSizeResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminSizeService;
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
@RequestMapping("/admin/sizes")
public class AdminSizeController {
    @Autowired
    private AdminSizeService adminSizeService;

    @GetMapping("")
    public Object findAllSize(AdminSizeRequest SizeDTO){
        PageableObject<AdminSizeResponse> listSize = adminSizeService.findAllEntity(SizeDTO);
        return ResponseHelper.getResponse(listSize, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") String id) {
        AdminSizeResponse adminSizeResponse = adminSizeService.findById(UUID.fromString(id));
        return ResponseHelper.getResponse(adminSizeResponse,HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminSizeRequest adminSizeRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminSizeService.create(adminSizeRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminSizeRequest adminSizeRequest, BindingResult bindingResult){
        adminSizeRequest.setId(UUID.fromString(id));

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminSizeService.update(adminSizeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminSizeService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}
