package com.ndt.be_stepupsneaker.core.admin.mapper.empolyee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminEmployeeMapper {
    AdminEmployeeMapper INSTANCE = Mappers.getMapper(AdminEmployeeMapper.class);

    AdminEmployeeResponse employeeToAdminEmpolyeeResponse(Employee employee);

    @Mapping(target = "role.id", source = "role")
    Employee adminEmployeeResquestToEmPolyee(AdminEmployeeRequest employeeDTO);
}
