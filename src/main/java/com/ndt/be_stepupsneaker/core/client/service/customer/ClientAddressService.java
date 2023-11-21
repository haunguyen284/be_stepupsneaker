package com.ndt.be_stepupsneaker.core.client.service.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientAddressResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientAddressService extends BaseService<ClientAddressResponse, String, ClientAddressRequest> {
    PageableObject<ClientAddressResponse> findAllAddress(String customerId, ClientAddressRequest addressRequest);
    Boolean updateDefaultAddressByCustomer(String addressId);
}
