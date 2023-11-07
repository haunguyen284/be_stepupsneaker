package com.ndt.be_stepupsneaker.core.admin.controller.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.service.employee.AdminEmployeeService;
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
@RequestMapping("/admin/employees")
public class AdminEmployeeController {
    @Autowired
    private AdminEmployeeService adminEmployeeService;

    @GetMapping("")
    public Object findAllEmployee(AdminEmployeeRequest employeeDTO){
        PageableObject<AdminEmployeeResponse> listEmplouyee = adminEmployeeService.findAllEntity(employeeDTO);
        return ResponseHelper.getResponse(listEmplouyee, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id")String id){
        AdminEmployeeResponse employeeResponse = adminEmployeeService.findById(UUID.fromString(id));

        return ResponseHelper.getResponse(employeeResponse, HttpStatus.OK);
    }

    @PostMapping("")
    public Object create(@RequestBody @Valid AdminEmployeeRequest employeeDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminEmployeeService.create(employeeDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable("id")String id, @RequestBody @Valid AdminEmployeeRequest employeeRequest, BindingResult  bindingResult){
        employeeRequest.setId(UUID.fromString(id));
        if (bindingResult.hasErrors()){
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse(adminEmployeeService.update(employeeRequest), HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id){
        return ResponseHelper.getResponse(adminEmployeeService.delete(UUID.fromString(id)), HttpStatus.OK);
    }
}
