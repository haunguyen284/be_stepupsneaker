package com.ndt.be_stepupsneaker.core.admin.service.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.infrastructure.security.auth.request.ChangePasswordRequest;

import java.util.UUID;

public interface AdminEmployeeService extends BaseService<AdminEmployeeResponse, String, AdminEmployeeRequest> {

    AdminEmployeeResponse changePassword(ChangePasswordRequest request);
}
