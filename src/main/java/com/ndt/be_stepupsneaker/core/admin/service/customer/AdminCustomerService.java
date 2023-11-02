package com.ndt.be_stepupsneaker.core.admin.service.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface AdminCustomerService extends BaseService<AdminCustomerResponse, UUID, AdminCustomerRequest> {

//    AdminCustomerResponse createAddressForCustomer(List<AdminAddressRequest> voucherRequestList, List<AdminCustomerRequest> adminCustomerRequests);


}
