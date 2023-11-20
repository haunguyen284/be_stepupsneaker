package com.ndt.be_stepupsneaker.core.admin.controller.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSoleResponse;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminSoleService;
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
@RequestMapping("/admin/soles")
public class SoleController {
    @Autowired
    private AdminSoleService adminSoleService;

    @GetMapping("")
    public Object findAllSole(AdminSoleRequest colorDTO){
        PageableObject<AdminSoleResponse> listSole = adminSoleService.findAllEntity(colorDTO);

        return ResponseHelper.getResponse(listSole, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminSoleResponse adminSoleResponse = adminSoleService.findById(id);

        return ResponseHelper.getResponse(adminSoleResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminSoleRequest colorDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminSoleService.create(colorDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminSoleRequest colorDTO, BindingResult bindingResult){
        colorDTO.setId(id);

        if (bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(adminSoleService.update(colorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id")String id){
        return ResponseHelper.getResponse(adminSoleService.delete(id), HttpStatus.OK);
    }
}
