package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSoleResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminSoleMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminSoleRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminSoleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Sole;
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
public class AdminSoleServiceImpl implements AdminSoleService {
    @Autowired
    private AdminSoleRepository adminSoleRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminSoleResponse> findAllEntity(AdminSoleRequest soleRequest) {

        Pageable pageable = paginationUtil.pageable(soleRequest);
        Page<Sole> resp = adminSoleRepository.findAllSole(soleRequest, soleRequest.getStatus(), pageable);
        Page<AdminSoleResponse> adminSoleResponses = resp.map(AdminSoleMapper.INSTANCE::colorToAdminSoleResponse);
        return new PageableObject<>(adminSoleResponses);
    }

    @Override
    public AdminSoleResponse create(AdminSoleRequest soleRequest) {
        Optional<Sole> brandOptional = adminSoleRepository.findByName(soleRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        Sole sole = adminSoleRepository.save(AdminSoleMapper.INSTANCE.adminSoleRequestToSole(soleRequest));

        return AdminSoleMapper.INSTANCE.colorToAdminSoleResponse(sole);
    }

    @Override
    public AdminSoleResponse update(AdminSoleRequest soleRequest) {
        Optional<Sole> brandOptional = adminSoleRepository.findByName(soleRequest.getId(), soleRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        brandOptional = adminSoleRepository.findById(soleRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("Sole IS NOT EXIST");
        }
        Sole soleSave = brandOptional.get();
        soleSave.setName(soleRequest.getName());
        soleSave.setStatus(soleRequest.getStatus());
        return AdminSoleMapper.INSTANCE.colorToAdminSoleResponse(adminSoleRepository.save(soleSave));
    }

    @Override
    public AdminSoleResponse findById(UUID id) {
        Optional<Sole> soleOptional = adminSoleRepository.findById(id);
        if (soleOptional.isEmpty()){
            throw new ResourceNotFoundException("MATERIAL IS NOT EXIST");
        }

        return AdminSoleMapper.INSTANCE.colorToAdminSoleResponse(soleOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Sole> soleOptional = adminSoleRepository.findById(id);
        if (soleOptional.isEmpty()){
            throw new ResourceNotFoundException("MATERIAL NOT FOUND");
        }
        Sole sole = soleOptional.get();
        sole.setDeleted(true);
        adminSoleRepository.save(sole);
        return true;
    }
}
