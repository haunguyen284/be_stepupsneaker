package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminAddressRepository;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressUtil {
    private final AdminAddressRepository adminAddressRepository;

    public Address updateAddress(Address address, AdminAddressRequest addressRequest) {
        if (addressRequest.getProvinceName().equals("")) {
            address.setProvinceName(address.getProvinceName());
        } else {
            address.setProvinceName(addressRequest.getProvinceName());
        }
        if (addressRequest.getDistrictName().equals("")) {
            address.setDistrictName(address.getDistrictName());
        } else {
            address.setDistrictName(addressRequest.getDistrictName());
        }
        if (addressRequest.getWardName().equals("")) {
            address.setWardName(address.getWardName());
        } else {
            address.setWardName(addressRequest.getWardName());
        }
        address.setMore(addressRequest.getMore());
        address.setDistrictId(addressRequest.getDistrictId());
        address.setProvinceId(addressRequest.getProvinceId());
        address.setWardCode(addressRequest.getWardCode());
        return address;
    }
}
