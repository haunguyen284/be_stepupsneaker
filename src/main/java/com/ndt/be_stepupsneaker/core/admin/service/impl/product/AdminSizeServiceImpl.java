package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSizeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSizeResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminSizeMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminSizeRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminSizeService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Size;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminSizeServiceImpl implements AdminSizeService {
    @Autowired
    private AdminSizeRepository adminSizeRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminSizeResponse> findAllEntity(AdminSizeRequest adminSizeRequest) {

        Pageable pageable = paginationUtil.pageable(adminSizeRequest);
        Page<Size> resp = adminSizeRepository.findAllSize(adminSizeRequest, adminSizeRequest.getStatus(), pageable);
        Page<AdminSizeResponse> adminSizeResponses = resp.map(AdminSizeMapper.INSTANCE::sizeToAdminSizeResponse);
        return new PageableObject<>(adminSizeResponses);
    }

    @Override
    public Object create(AdminSizeRequest adminSizeRequest) {
        Optional<Size> optionalSize = adminSizeRepository.findByName(adminSizeRequest.getName());
        if (optionalSize.isPresent()) {
            throw new ApiException("NAME IS EXIST !");
        }

        Size size = adminSizeRepository.save(AdminSizeMapper.INSTANCE.adminSizeRequestToSize(adminSizeRequest));

        return AdminSizeMapper.INSTANCE.sizeToAdminSizeResponse(size);
    }

    @Override
    public AdminSizeResponse update(AdminSizeRequest adminSizeRequest) {
        Optional<Size> optionalSize = adminSizeRepository.findByName(adminSizeRequest.getId(), adminSizeRequest.getName());
        if (optionalSize.isPresent()) {
            throw new ApiException("NAME IS EXIST !");
        }

        optionalSize = adminSizeRepository.findById(adminSizeRequest.getId());
        if (optionalSize.isEmpty()) {
            throw new ResourceNotFoundException("SIZE IS NOT EXIST !");
        }
        Size newSize = optionalSize.get();
        newSize.setName(adminSizeRequest.getName());
        newSize.setStatus(adminSizeRequest.getStatus());
        return AdminSizeMapper.INSTANCE.sizeToAdminSizeResponse(adminSizeRepository.save(newSize));
    }

    @Override
    public AdminSizeResponse findById(String id) {
        Optional<Size> optionalSize = adminSizeRepository.findById(id);
        if (optionalSize.isEmpty()) {
            throw new ResourceNotFoundException("SIZE IS NOT EXIST :" + id);
        }
        return AdminSizeMapper.INSTANCE.INSTANCE.sizeToAdminSizeResponse(optionalSize.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Size> optionalSize = adminSizeRepository.findById(id);
        if (optionalSize.isEmpty()) {
            throw new ResourceNotFoundException("VOUCHER NOT FOUND :" + id);
        }
        Size newSize = optionalSize.get();
        newSize.setDeleted(true);
        adminSizeRepository.save(newSize);
        return true;
    }
}
