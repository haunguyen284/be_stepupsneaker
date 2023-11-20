package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminStyleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminStyleResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminStyleService;
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
@RequestMapping("/admin/styles")
public class AdminStyleController {
    @Autowired
    private AdminStyleService adminStyleService;

    @GetMapping("")
    public Object findAllStyle(AdminStyleRequest colorDTO){
        PageableObject<AdminStyleResponse> listStyle = adminStyleService.findAllEntity(colorDTO);

        return ResponseHelper.getResponse(listStyle, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminStyleResponse adminStyleResponse = adminStyleService.findById(id);

        return ResponseHelper.getResponse(adminStyleResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminStyleRequest colorDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminStyleService.create(colorDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminStyleRequest colorDTO, BindingResult bindingResult){
        colorDTO.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminStyleService.update(colorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminStyleService.delete(id), HttpStatus.OK);
    }
}