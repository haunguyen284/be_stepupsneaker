package com.ndt.be_stepupsneaker.core.admin.service.impl.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminAddressRepository;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminAddressService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminAddressServiceImpl implements AdminAddressService {

    @Autowired
    private AdminAddressRepository adminAddressRepository;
    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminAddressResponse> findAllEntity(AdminAddressRequest addressRequest) {
        Pageable pageable = paginationUtil.pageable(addressRequest);
        Page<Address> resp = adminAddressRepository.findAllAddress(addressRequest, pageable);
        Page<AdminAddressResponse> adminAddressResponses = resp.map(AdminAddressMapper.INSTANCE::addressToAdminAddressResponse);

        return new PageableObject<>(adminAddressResponses);
    }

    @Override
    public AdminAddressResponse create(AdminAddressRequest addressDTO) {
        Optional<Address> addressOptional = adminAddressRepository.findByPhoneNumber(addressDTO.getPhoneNumber());
        if (addressOptional.isPresent()){
            throw new ApiException(("Phone is exits"));
        }
        Address address = adminAddressRepository.save(AdminAddressMapper.INSTANCE.adminAddressRequestAddress(addressDTO));

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(address);
    }

    @Override
    public AdminAddressResponse update(AdminAddressRequest addressDTO) {
        Optional<Address> addressOptional= adminAddressRepository.findByPhoneNumber(addressDTO.getId(), addressDTO.getPhoneNumber());
        if(addressOptional.isPresent()){
            throw new ApiException(("Phone is exit"));
        }
        addressOptional = adminAddressRepository.findById(addressDTO.getId());
        if (addressOptional.isPresent()){
            throw new ResourceNotFoundException("Phone is not exit");
        }
        Address addressSave = addressOptional.get();
        addressSave.setCity(addressDTO.getCity());
        addressSave.setCountry(addressDTO.getCountry());
        addressSave.setIsDefault(addressDTO.getIsDefault());
        addressSave.setProvince(addressDTO.getProvince());
        addressSave.setPhoneNumber(addressDTO.getPhoneNumber());

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(adminAddressRepository.save(addressSave));
    }

    @Override
    public AdminAddressResponse findById(UUID id) {
        Optional<Address> addressOptional = adminAddressRepository.findById(id);
        if (addressOptional.isEmpty()){
            throw new RuntimeException("LOOI");
        }

        return AdminAddressMapper.INSTANCE.addressToAdminAddressResponse(addressOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Address> addressOptional =adminAddressRepository.findById(id);
        if (addressOptional.isEmpty()){
            throw new ResourceNotFoundException("Phone Not Found");
        }
        adminAddressRepository.delete(addressOptional.get());
        return true;
    }
}
