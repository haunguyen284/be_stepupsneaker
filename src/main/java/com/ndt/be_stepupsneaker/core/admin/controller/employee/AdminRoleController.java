package com.ndt.be_stepupsneaker.core.admin.controller.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminRoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminRoleRsponse;
import com.ndt.be_stepupsneaker.core.admin.service.employee.AdminRoleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/admin/role")
public class AdminRoleController {
    @Autowired
    private AdminRoleService adminRoleService;

    @GetMapping("")
    public Object findAllRole(AdminRoleRequest roleDTO){
        PageableObject<AdminRoleRsponse> listRole = adminRoleService.findAllEntity(roleDTO);
        return ResponseHelper.getResponse(listRole, HttpStatus.OK);

    }
    @PostMapping("")
    public Object create(@RequestBody @Valid AdminRoleRequest roleDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);

        }
        return ResponseHelper.getResponse(adminRoleService.create(roleDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id") String id, @RequestBody @Valid AdminRoleRequest roleRequest, BindingResult bindingResult){
        roleRequest.setId(UUID.fromString(id));
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminRoleService.update(roleRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id){
        return ResponseHelper.getResponse(adminRoleService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}
