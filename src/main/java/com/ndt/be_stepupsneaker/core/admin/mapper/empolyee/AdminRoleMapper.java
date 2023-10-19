package com.ndt.be_stepupsneaker.core.admin.mapper.empolyee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminRoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminRoleRsponse;
import com.ndt.be_stepupsneaker.entity.employee.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminRoleMapper {
    AdminRoleMapper INSTANCE = Mappers.getMapper(AdminRoleMapper.class);

    AdminRoleRsponse roleToAdminRoleResponse(Role role);

    Role adminRoleRequestToRole(AdminRoleRequest roleDTO);

}
