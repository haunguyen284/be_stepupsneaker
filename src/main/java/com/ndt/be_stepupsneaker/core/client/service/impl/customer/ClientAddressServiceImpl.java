package com.ndt.be_stepupsneaker.core.client.service.impl.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientAddressResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientAddressMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.service.customer.ClientAddressService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientAddressServiceImpl implements ClientAddressService {

    @Autowired
    private ClientAddressRepository clientAddressRepository;

    @Autowired
    private ClientCustomerRepository clientCustomerRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MySessionInfo mySessionInfo;

    @Autowired
    private MessageUtil messageUtil;

    // Not user funciton
    @Override
    public PageableObject<ClientAddressResponse> findAllEntity(ClientAddressRequest addressRequest) {
        return null;
    }

    // Tạo address bắt buộc phải cho id customer
    @Override
    public Object create(ClientAddressRequest addressDTO) {
        Optional<Customer> customerOptional = clientCustomerRepository.findById(addressDTO.getCustomer());
        if (!customerOptional.isPresent()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("customer.notfound"));
        }
        List<Address> addressList = clientAddressRepository.findByCustomerAndDeleted(customerOptional.get(),false);
        if (addressList.size() >= 3) {
            throw new ResourceNotFoundException(messageUtil.getMessage("customer.max_address"));
        }
        boolean hasAddress = clientAddressRepository.existsByCustomer(customerOptional.get());
        Address address = ClientAddressMapper.INSTANCE.clientAddressRequestToAddress(addressDTO);
        if (!hasAddress) {
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        address.setCustomer(customerOptional.get());
        address = clientAddressRepository.save(address);
        ClientAddressResponse ClientAddressResponse = ClientAddressMapper.INSTANCE.addressToClientAddressResponse(address);
        return ClientAddressResponse;
    }

    @Override
    public ClientAddressResponse update(ClientAddressRequest addressDTO) {
        Optional<Address> addressOptional = clientAddressRepository.findById(addressDTO.getId());
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }
        Address addressSave = addressOptional.get();
        if(addressDTO.getProvinceName()!=null){
            addressSave.setProvinceName(addressDTO.getProvinceName());
        }
        if(addressDTO.getDistrictName()!=null){
            addressSave.setDistrictName(addressDTO.getDistrictName());
        }
        if(addressDTO.getWardName()!=null){
            addressSave.setWardName(addressDTO.getWardName());
        }

        addressSave.setDistrictId(addressDTO.getDistrictId());
        addressSave.setWardCode(addressDTO.getWardCode());
        addressSave.setProvinceId(addressDTO.getProvinceId());
        addressSave.setMore(addressDTO.getMore());
        addressSave.setPhoneNumber(addressDTO.getPhoneNumber());
        return ClientAddressMapper.INSTANCE.addressToClientAddressResponse(clientAddressRepository.save(addressSave));
    }

    @Override
    public ClientAddressResponse findById(String id) {
        Optional<Address> addressOptional = clientAddressRepository.findByIdAndCustomer(id, currentCustomer());
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }

        return ClientAddressMapper.INSTANCE.addressToClientAddressResponse(addressOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Address> addressOptional = clientAddressRepository.findByIdAndCustomer(id,currentCustomer());
        if (addressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }
        Address address = addressOptional.get();
        address.setDeleted(true);
        clientAddressRepository.save(address);
        return true;
    }

    @Override
    public PageableObject<ClientAddressResponse> findAllAddress(String customerId, ClientAddressRequest addressRequest) {
        Pageable pageable = paginationUtil.pageable(addressRequest);
        Page<Address> addressPage = clientAddressRepository.findAllAddress(customerId, addressRequest, pageable);
        Page<ClientAddressResponse> ClientAddressRespPage = addressPage.map(ClientAddressMapper.INSTANCE::addressToClientAddressResponse);
        return new PageableObject<>(ClientAddressRespPage);
    }

    @Override
    public Boolean updateDefaultAddressByCustomer(String addressId) {
        Optional<Address> newDefaultAddressOptional = clientAddressRepository.findById(addressId);
        if (newDefaultAddressOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("address.notfound"));
        }
        Address existingDefaultAddress = clientAddressRepository.findDefaultAddressByCustomer(newDefaultAddressOptional.get().getCustomer().getId());
        if (existingDefaultAddress != null) {
            existingDefaultAddress.setIsDefault(false);
            clientAddressRepository.save(existingDefaultAddress);
        }
        Address newDefaultAddress = newDefaultAddressOptional.get();
        newDefaultAddress.setIsDefault(true);
        clientAddressRepository.save(newDefaultAddress);
        return true;
    }

    private Customer currentCustomer() {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        Customer customer = clientCustomerRepository.findById(customerResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("customer.notfound")));
        return customer;
    }
}
