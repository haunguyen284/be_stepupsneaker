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
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminBrandServiceImpl implements AdminBrandService {
    @Autowired
    private AdminBrandRepository adminBrandRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public PageableObject<AdminBrandResponse> findAllEntity(AdminBrandRequest brandRequest) {

        Pageable pageable = paginationUtil.pageable(brandRequest);
        Page<Brand> resp = adminBrandRepository.findAllBrand(brandRequest, brandRequest.getStatus(), pageable);
        Page<AdminBrandResponse> adminBrandResponses = resp.map(AdminBrandMapper.INSTANCE::BrandToAdminBrandResponse);
        return new PageableObject<>(adminBrandResponses);
    }

    @Override
    public Object create(AdminBrandRequest BrandDTO) {
        Optional<Brand> brandOptional = adminBrandRepository.findByName(BrandDTO.getName());
        if (brandOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.brand.name.exist"));
        }
        Brand Brand = adminBrandRepository.save(AdminBrandMapper.INSTANCE.adminBrandRequestToBrand(BrandDTO));

        return AdminBrandMapper.INSTANCE.BrandToAdminBrandResponse(Brand);
    }

    @Override
    public AdminBrandResponse update(AdminBrandRequest brandRequest) {
        Optional<Brand> brandOptional = adminBrandRepository.findByName(brandRequest.getId(), brandRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.brand.name.exist"));
        }

        brandOptional = adminBrandRepository.findById(brandRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.brand.notfound"));
        }
        Brand BrandSave = brandOptional.get();
        BrandSave.setName(brandRequest.getName());
        return AdminBrandMapper.INSTANCE.BrandToAdminBrandResponse(adminBrandRepository.save(BrandSave));
    }

    @Override
    public AdminBrandResponse findById(String id) {
        Optional<Brand> BrandOptional = adminBrandRepository.findById(id);
        if (BrandOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.brand.notfound"));
        }

        return AdminBrandMapper.INSTANCE.BrandToAdminBrandResponse(BrandOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Brand> brandOptional = adminBrandRepository.findById(id);
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.brand.notfound"));
        }
        Brand brand = brandOptional.get();
        brand.setDeleted(true);
        adminBrandRepository.save(brand);
        return true;
    }
}
