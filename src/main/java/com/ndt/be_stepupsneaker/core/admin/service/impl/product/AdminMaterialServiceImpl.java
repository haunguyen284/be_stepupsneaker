package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminMaterialRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminMaterialResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminMaterialMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminMaterialRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminMaterialService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Material;
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
public class AdminMaterialServiceImpl implements AdminMaterialService {
    @Autowired
    private AdminMaterialRepository adminMaterialRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminMaterialResponse> findAllEntity(AdminMaterialRequest brandRequest) {

        Pageable pageable = paginationUtil.pageable(brandRequest);
        Page<Material> resp = adminMaterialRepository.findAllMaterial(brandRequest, brandRequest.getStatus(), pageable);
        Page<AdminMaterialResponse> adminMaterialResponses = resp.map(AdminMaterialMapper.INSTANCE::colorToAdminMaterialResponse);
        return new PageableObject<>(adminMaterialResponses);
    }

    @Override
    public AdminMaterialResponse create(AdminMaterialRequest MaterialDTO) {
        Optional<Material> brandOptional = adminMaterialRepository.findByName(MaterialDTO.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        Material Material = adminMaterialRepository.save(AdminMaterialMapper.INSTANCE.adminMaterialRequestToMaterial(MaterialDTO));

        return AdminMaterialMapper.INSTANCE.colorToAdminMaterialResponse(Material);
    }

    @Override
    public AdminMaterialResponse update(AdminMaterialRequest brandRequest) {
        Optional<Material> brandOptional = adminMaterialRepository.findByName(brandRequest.getId(), brandRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        brandOptional = adminMaterialRepository.findById(brandRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("Material IS NOT EXIST");
        }
        Material MaterialSave = brandOptional.get();
        MaterialSave.setName(brandRequest.getName());
        MaterialSave.setStatus(brandRequest.getStatus());
        return AdminMaterialMapper.INSTANCE.colorToAdminMaterialResponse(adminMaterialRepository.save(MaterialSave));
    }

    @Override
    public AdminMaterialResponse findById(UUID id) {
        Optional<Material> MaterialOptional = adminMaterialRepository.findById(id);
        if (MaterialOptional.isEmpty()){
            throw new ResourceNotFoundException("MATERIAL IS NOT EXIST");
        }

        return AdminMaterialMapper.INSTANCE.colorToAdminMaterialResponse(MaterialOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Material> brandOptional = adminMaterialRepository.findById(id);
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("MATERIAL NOT FOUND");
        }
        Material brand = brandOptional.get();
        brand.setDeleted(true);
        adminMaterialRepository.save(brand);
        return true;
    }
}