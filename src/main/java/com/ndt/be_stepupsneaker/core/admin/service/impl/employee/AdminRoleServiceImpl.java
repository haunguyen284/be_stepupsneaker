package com.ndt.be_stepupsneaker.core.admin.service.impl.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminRoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminRoleRsponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.empolyee.AdminRoleMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminRoleRepository;
import com.ndt.be_stepupsneaker.core.admin.service.employee.AdminRoleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.employee.Role;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminRoleRsponse> findAllEntity(AdminRoleRequest roleRequest) {
        Pageable pageable = paginationUtil.pageable(roleRequest);
        Page<Role> resp = adminRoleRepository.findAllRole(roleRequest, pageable);
        Page<AdminRoleRsponse> adminRoleRsponses = resp.map(AdminRoleMapper.INSTANCE::roleToAdminRoleResponse);

        return new PageableObject<>(adminRoleRsponses);
    }

    @Override
    public Object create(AdminRoleRequest roleDTO) {
        Optional<Role> optionalRole = adminRoleRepository.findByName(roleDTO.getName());
        if (optionalRole.isPresent()){
            throw new ApiException("Role name is exist");
        }

        Role role = adminRoleRepository.save(AdminRoleMapper.INSTANCE.adminRoleRequestToRole(roleDTO));

        return AdminRoleMapper.INSTANCE.roleToAdminRoleResponse(role);
    }
    @Override
    public AdminRoleRsponse update(AdminRoleRequest roleDTO) {
        Optional<Role> optionalRole = adminRoleRepository.findByName(roleDTO.getName());
        if (optionalRole.isPresent()){
            throw new ApiException("Role name is exist");
        }
        optionalRole = adminRoleRepository.findById(roleDTO.getId());
        if (optionalRole.isEmpty()){
            throw new ResourceNotFoundException("Role is nt exist");
        }
        Role role = optionalRole.get();
        role.setName(roleDTO.getName());

        return AdminRoleMapper.INSTANCE.roleToAdminRoleResponse(adminRoleRepository.save(role));
    }
    @Override
    public AdminRoleRsponse findById(String id) {
        Optional<Role> optionalRole = adminRoleRepository.findById(id);
        if (optionalRole.isEmpty()){
            throw  new RuntimeException("LOOI");

        }
        return AdminRoleMapper.INSTANCE.roleToAdminRoleResponse(optionalRole.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Role> optionalRole = adminRoleRepository.findById(id);
        if (optionalRole.isEmpty()){
            throw  new RuntimeException("Role is not exist");
        }
        Role role = optionalRole.get();
        role.setDeleted(true);
        adminRoleRepository.save(role);
        return true;
    }

}
