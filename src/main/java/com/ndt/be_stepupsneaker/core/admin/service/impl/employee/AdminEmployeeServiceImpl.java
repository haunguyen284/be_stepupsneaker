package com.ndt.be_stepupsneaker.core.admin.service.impl.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.empolyee.AdminEmployeeMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminRoleRepository;
import com.ndt.be_stepupsneaker.core.admin.service.employee.AdminEmployeeService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.employee.Role;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminEmployeeServiceImpl implements AdminEmployeeService {

    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    @Autowired
    private AdminEmployeeRepository adminEmployeeRepository;

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminEmployeeResponse> findAllEntity(AdminEmployeeRequest employeeRequest) {
        Pageable pageable = paginationUtil.pageable(employeeRequest);
        Page<Employee> resp = adminEmployeeRepository.findAllEmployee(employeeRequest, employeeRequest.getStatus(), pageable);
        Page<AdminEmployeeResponse> adminEmployeeResponses = resp.map(AdminEmployeeMapper.INSTANCE::employeeToAdminEmpolyeeResponse);
        return new PageableObject<>(adminEmployeeResponses);

    }

    @Override
    public Object create(AdminEmployeeRequest employeeDTO) {
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmail(employeeDTO.getEmail());
        if (employeeOptional.isPresent()) {
            throw new ApiException("Email is exist");
        }
        employeeOptional = adminEmployeeRepository.findByPhoneNumber(employeeDTO.getPhoneNumber());
        if (employeeOptional.isPresent()) {
            throw new ApiException("PhoneNumber is exist");
        }
        employeeDTO.setImage(cloudinaryUpload.upload(employeeDTO.getImage()));
        Employee employee = adminEmployeeRepository.save(AdminEmployeeMapper.INSTANCE.adminEmployeeResquestToEmPolyee(employeeDTO));

        return AdminEmployeeMapper.INSTANCE.employeeToAdminEmpolyeeResponse(employee);
    }

    @Override
    public AdminEmployeeResponse update(AdminEmployeeRequest employeeDTO) {
        Optional<Employee> employeeOptional = adminEmployeeRepository.findByEmail(employeeDTO.getId(), employeeDTO.getEmail());
        if (employeeOptional.isPresent()) {
            throw new ApiException("Email is exist");
        }
        employeeOptional = adminEmployeeRepository.findById(employeeDTO.getId());
        if (employeeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Employee not exist");
        }

        Optional<Role> roleOptional = adminRoleRepository.findById(employeeDTO.getRole());
        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException("Role not exist");
        }
        Employee employee = employeeOptional.get();
        employee.setPassword(employeeDTO.getPassword());
        employee.setGender(employeeDTO.getGender());
        employee.setEmail(employeeDTO.getEmail());
        employee.setStatus(employeeDTO.getStatus());
        employee.setAddress(employeeDTO.getAddress());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setFullName(employeeDTO.getFullName());
        employee.setImage(cloudinaryUpload.upload(employeeDTO.getImage()));
        employee.setRole(roleOptional.get());

        return AdminEmployeeMapper.INSTANCE.employeeToAdminEmpolyeeResponse(adminEmployeeRepository.save(employee));
    }

    @Override
    public AdminEmployeeResponse findById(String id) {
        Optional<Employee> employeeOptional = adminEmployeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new RuntimeException("LOOI");
        }
        return AdminEmployeeMapper.INSTANCE.employeeToAdminEmpolyeeResponse(employeeOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Employee> employeeOptional = adminEmployeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Employee is not found");
        }
        Employee employee = employeeOptional.get();
        employee.setDeleted(true);
        adminEmployeeRepository.save(employee);
        return true;
    }
}
