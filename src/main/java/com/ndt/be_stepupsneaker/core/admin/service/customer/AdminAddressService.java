package com.ndt.be_stepupsneaker.core.admin.service.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.UUID;

public interface AdminAddressService extends BaseService<AdminAddressResponse, String, AdminAddressRequest> {
    PageableObject<AdminAddressResponse> findAllAddress(String customerId, AdminAddressRequest addressRequest);

    Boolean updateDefaultAddressByCustomer(String addressId);
}
