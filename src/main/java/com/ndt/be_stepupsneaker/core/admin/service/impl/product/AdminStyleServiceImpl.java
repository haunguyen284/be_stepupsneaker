package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminStyleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminStyleResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminStyleMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminStyleRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminStyleService;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminStyleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Style;
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
public class AdminStyleServiceImpl implements AdminStyleService {
    @Autowired
    private AdminStyleRepository adminStyleRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminStyleResponse> findAllEntity(AdminStyleRequest soleRequest) {

        Pageable pageable = paginationUtil.pageable(soleRequest);
        Page<Style> resp = adminStyleRepository.findAllStyle(soleRequest, soleRequest.getStatus(), pageable);
        Page<AdminStyleResponse> adminStyleResponses = resp.map(AdminStyleMapper.INSTANCE::colorToAdminStyleResponse);
        return new PageableObject<>(adminStyleResponses);
    }

    @Override
    public AdminStyleResponse create(AdminStyleRequest soleRequest) {
        Optional<Style> brandOptional = adminStyleRepository.findByName(soleRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        Style sole = adminStyleRepository.save(AdminStyleMapper.INSTANCE.adminStyleRequestToStyle(soleRequest));

        return AdminStyleMapper.INSTANCE.colorToAdminStyleResponse(sole);
    }

    @Override
    public AdminStyleResponse update(AdminStyleRequest soleRequest) {
        Optional<Style> brandOptional = adminStyleRepository.findByName(soleRequest.getId(), soleRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        brandOptional = adminStyleRepository.findById(soleRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("Style IS NOT EXIST");
        }
        Style soleSave = brandOptional.get();
        soleSave.setName(soleRequest.getName());
        soleSave.setStatus(soleRequest.getStatus());
        return AdminStyleMapper.INSTANCE.colorToAdminStyleResponse(adminStyleRepository.save(soleSave));
    }

    @Override
    public AdminStyleResponse findById(UUID id) {
        Optional<Style> soleOptional = adminStyleRepository.findById(id);
        if (soleOptional.isEmpty()){
            throw new ResourceNotFoundException("MATERIAL IS NOT EXIST");
        }

        return AdminStyleMapper.INSTANCE.colorToAdminStyleResponse(soleOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Style> soleOptional = adminStyleRepository.findById(id);
        if (soleOptional.isEmpty()){
            throw new ResourceNotFoundException("MATERIAL NOT FOUND");
        }
        Style sole = soleOptional.get();
        sole.setDeleted(true);
        adminStyleRepository.save(sole);
        return true;
    }
}
