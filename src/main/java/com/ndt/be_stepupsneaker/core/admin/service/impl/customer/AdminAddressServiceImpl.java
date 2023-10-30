package com.ndt.be_stepupsneaker.core.admin.service.impl.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminCustomerMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminAddressRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminAddressService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminAddressServiceImpl implements AdminAddressService {

    @Autowired
    private AdminAddressRepository adminAddressRepository;

    @Autowired
    private AdminCustomerRepository adminCustomerRepository;
    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminAddressResponse> findAllEntity(AdminAddressRequest addressRequest) {
        Pageable pageable = paginationUtil.pageable(addressRequest);
        Page<Address> resp = adminAddressRepository.findAllAddress(addressRequest, pageable);
        Page<AdminAddressResponse> adminAddressResponses = resp.map(AdminAddressMapper.INSTANCE::addressToAdminAddressResponse);

        return new PageableObject<>(adminAddressResponses);
    }

    // Tạo address bắt buộc phải cho id customer
    @Override
    public AdminAddressResponse create(AdminAddressRequest addressDTO) {
        AdminCustomerResponse adminCustomerResponse;
        Optional<Address> addressOptional = adminAddressRepository.findByPhoneNumber(addressDTO.getPhoneNumber());
        if (addressOptional.isPresent()) {
            throw new ResourceNotFoundException("PHONE IS EXISTS ! TAO QUÁ BUỒN ....");
        }
        Optional<Customer> customerOptional = adminCustomerRepository.findById(addressDTO.getCustomerRequest().getId());
        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException("CUSTOMER NOT FOUND ! TAO QUÁ BUỒN ....");
        }
        List<Address> addressList = adminAddressRepository.findByCustomer(customerOptional.get());
        if (addressList.size() >= 3) {
            throw new ResourceNotFoundException("Customers can only create a maximum of 3 addresses ! LEW LEW");
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
        adminCustomerResponse = AdminCustomerMapper.INSTANCE.customerToAdminCustomerResponse(customerOptional.get());
        AdminAddressResponse adminAddressResponse = AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(address);
        adminAddressResponse.setCustomerResponse(adminCustomerResponse);
        return adminAddressResponse;
    }

    @Override
    public AdminAddressResponse update(AdminAddressRequest addressDTO) {
        Optional<Address> addressOptional = adminAddressRepository.findByPhoneNumber(addressDTO.getId(), addressDTO.getPhoneNumber());
        if (addressOptional.isPresent()) {
            throw new ApiException(("Phone is exit"));
        }
        addressOptional = adminAddressRepository.findById(addressDTO.getId());
        if (addressOptional.isPresent()) {
            throw new ResourceNotFoundException("Phone is not exit");
        }
        Address addressSave = addressOptional.get();
        addressSave.setDistrict(addressDTO.getDistrict());
        addressSave.setWard(addressDTO.getWard());
        addressSave.setMore(addressDTO.getMore());
//        addressSave.setIsDefault(addressDTO.getIsDefault());
        addressSave.setProvince(addressDTO.getProvince());
        addressSave.setPhoneNumber(addressDTO.getPhoneNumber());

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(adminAddressRepository.save(addressSave));
    }

    @Override
    public AdminAddressResponse findById(UUID id) {
        Optional<Address> addressOptional = adminAddressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException("Address Not Found");
        }

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(addressOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Address> addressOptional = adminAddressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException("Address Not Found");
        }

        Address address = addressOptional.get();
        address.setDeleted(true);
        adminAddressRepository.save(address);
        return true;
    }

    @Override
    public PageableObject<AdminAddressResponse> findAllAddressByCustomerId(UUID customerId, AdminAddressRequest addressRequest) {
        Pageable pageable = paginationUtil.pageable(addressRequest);
        Page<Address> addressPage = adminAddressRepository.findAllAddressByCustomerId(customerId, addressRequest, pageable);
        Page<AdminAddressResponse> adminAddressRespPage = addressPage.map(AdminAddressMapper.INSTANCE::addressToAdminAddressResponse);
        return new PageableObject<>(adminAddressRespPage);
    }

    @Override
    public Boolean updateDefaultAddressByCustomer(UUID customerId, UUID addressId) {
        Address existingDefaultAddress = adminAddressRepository.findDefaultAddressByCustomer(customerId);
        if (existingDefaultAddress != null) {
            existingDefaultAddress.setIsDefault(false);
            adminAddressRepository.save(existingDefaultAddress);
        }
        Optional<Address> newDefaultAddressOptional = adminAddressRepository.findById(addressId);
        if (newDefaultAddressOptional.isEmpty()) {
            throw new ResourceNotFoundException("Address Not Found ! TAO QUÁ MỆT MỎI !");
        }
        Address newDefaultAddress = newDefaultAddressOptional.get();
        newDefaultAddress.setIsDefault(true);
        adminAddressRepository.save(newDefaultAddress);
        return true;
    }
}
