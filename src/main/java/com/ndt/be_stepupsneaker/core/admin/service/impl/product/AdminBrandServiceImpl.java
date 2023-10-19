package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminBrandMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminBrandRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminBrandService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Brand;
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
public class AdminBrandServiceImpl implements AdminBrandService {
    @Autowired
    private AdminBrandRepository adminBrandRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminBrandResponse> findAllEntity(AdminBrandRequest brandRequest) {

        Pageable pageable = paginationUtil.pageable(brandRequest);
        Page<Brand> resp = adminBrandRepository.findAllBrand(brandRequest, brandRequest.getStatus(), pageable);
        Page<AdminBrandResponse> adminBrandResponses = resp.map(AdminBrandMapper.INSTANCE::BrandToAdminBrandResponse);
        return new PageableObject<>(adminBrandResponses);
    }

    @Override
    public AdminBrandResponse create(AdminBrandRequest BrandDTO) {
        Optional<Brand> brandOptional = adminBrandRepository.findByName(BrandDTO.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        Brand Brand = adminBrandRepository.save(AdminBrandMapper.INSTANCE.adminBrandRequestToBrand(BrandDTO));

        return AdminBrandMapper.INSTANCE.BrandToAdminBrandResponse(Brand);
    }

    @Override
    public AdminBrandResponse update(AdminBrandRequest brandRequest) {
        Optional<Brand> brandOptional = adminBrandRepository.findByName(brandRequest.getId(), brandRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        brandOptional = adminBrandRepository.findById(brandRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("Brand IS NOT EXIST");
        }
        Brand BrandSave = brandOptional.get();
        BrandSave.setName(brandRequest.getName());
        BrandSave.setStatus(brandRequest.getStatus());
        return AdminBrandMapper.INSTANCE.BrandToAdminBrandResponse(adminBrandRepository.save(BrandSave));
    }

    @Override
    public AdminBrandResponse findById(UUID id) {
        Optional<Brand> BrandOptional = adminBrandRepository.findById(id);
        if (BrandOptional.isEmpty()){
            throw new ResourceNotFoundException("BRAND IS NOT EXIST");
        }

        return AdminBrandMapper.INSTANCE.BrandToAdminBrandResponse(BrandOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Brand> brandOptional = adminBrandRepository.findById(id);
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("BRAND NOT FOUND");
        }
        Brand brand = brandOptional.get();
        brand.setDeleted(true);
        adminBrandRepository.save(brand);
        return true;
    }
}
