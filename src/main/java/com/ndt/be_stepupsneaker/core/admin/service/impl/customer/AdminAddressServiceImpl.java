package com.ndt.be_stepupsneaker.core.admin.service.impl.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminAddressRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminAddressService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminAddressServiceImpl implements AdminAddressService {

    @Autowired
    private AdminAddressRepository adminAddressRepository;

    @Autowired
    private AdminCustomerRepository adminCustomerRepository;

    @Autowired
    private PaginationUtil paginationUtil;
    
    @Autowired
    private MessageUtil messageUtil;

    @Override
    public PageableObject<AdminAddressResponse> findAllEntity(AdminAddressRequest addressRequest) {
        return null;
    }

    @Override
    public Object create(AdminAddressRequest addressDTO) {
        Optional<Address> addressOptional = adminAddressRepository.findByPhoneNumber(addressDTO.getPhoneNumber());
        if (addressOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.phone.exist"));
        }
        Optional<Customer> customerOptional = adminCustomerRepository.findById(addressDTO.getCustomer());
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("customer.notfound"));
        }
        List<Address> addressList = adminAddressRepository.findByCustomer(customerOptional.get());
        if (addressList.size() >= 3) {
            throw new ApiException(messageUtil.getMessage("customer.max_address"));
        }
        boolean hasAddress = adminAddressRepository.existsByCustomer(customerOptional.get());
        Address address = AdminAddressMapper.INSTANCE.adminAddressRequestAddress(addressDTO);
        if (!hasAddress) {
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        address.setCustomer(customerOptional.get());
        address = adminAddressRepository.save(address);
        AdminAddressResponse adminAddressResponse = AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(address);
        return adminAddressResponse;
    }

    @Override
    public AdminAddressResponse update(AdminAddressRequest addressDTO) {
        Optional<Address> addressOptional = adminAddressRepository.findByPhoneNumber(addressDTO.getId(), addressDTO.getPhoneNumber());
        if (addressOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("address.phone.exist"));
        }
        addressOptional = adminAddressRepository.findById(addressDTO.getId());
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }
        Address addressSave = addressOptional.get();
        addressSave.setDistrictId(addressDTO.getDistrictId());
        addressSave.setWardCode(addressDTO.getWardCode());
        addressSave.setProvinceId(addressDTO.getProvinceId());
        addressSave.setDistrictName(addressDTO.getDistrictName());
        addressSave.setWardName(addressDTO.getWardName());
        addressSave.setProvinceName(addressDTO.getProvinceName());

        addressSave.setMore(addressDTO.getMore());
        addressSave.setPhoneNumber(addressDTO.getPhoneNumber());

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(adminAddressRepository.save(addressSave));
    }

    @Override
    public AdminAddressResponse findById(String id) {
        Optional<Address> addressOptional = adminAddressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(addressOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Address> addressOptional = adminAddressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }

        Address address = addressOptional.get();
        address.setDeleted(true);
        adminAddressRepository.save(address);
        return true;
    }

    @Override
    public PageableObject<AdminAddressResponse> findAllAddress(String customerId, AdminAddressRequest addressRequest) {
        Pageable pageable = paginationUtil.pageable(addressRequest);
        Page<Address> addressPage = adminAddressRepository.findAllAddress(customerId, addressRequest, pageable);
        Page<AdminAddressResponse> adminAddressRespPage = addressPage.map(AdminAddressMapper.INSTANCE::addressToAdminAddressResponse);
        return new PageableObject<>(adminAddressRespPage);
    }

    @Override
    public Boolean updateDefaultAddressByCustomer(String addressId) {
        Optional<Address> newDefaultAddressOptional = adminAddressRepository.findById(addressId);
        if (newDefaultAddressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }
        Address existingDefaultAddress = adminAddressRepository.findDefaultAddressByCustomer(newDefaultAddressOptional.get().getCustomer().getId());
        if (existingDefaultAddress != null) {
            existingDefaultAddress.setIsDefault(false);
            adminAddressRepository.save(existingDefaultAddress);
        }
        Address newDefaultAddress = newDefaultAddressOptional.get();
        newDefaultAddress.setIsDefault(true);
        adminAddressRepository.save(newDefaultAddress);
        return true;
    }
}
