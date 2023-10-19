package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Product;
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
public class AdminProductServiceImpl implements AdminProductService {
    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminProductResponse> findAllEntity(AdminProductRequest productRequest) {

        Pageable pageable = paginationUtil.pageable(productRequest);
        Page<Product> resp = adminProductRepository.findAllProduct(productRequest, productRequest.getStatus(), pageable);
        Page<AdminProductResponse> adminProductResponses = resp.map(AdminProductMapper.INSTANCE::productToAdminProductResponse);
        return new PageableObject<>(adminProductResponses);
    }

    @Override
    public AdminProductResponse create(AdminProductRequest productDTO) {
        Optional<Product> productOptional = adminProductRepository.findByName(productDTO.getName());
        if (productOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        productOptional = adminProductRepository.findByCode(productDTO.getCode());
        if (productOptional.isPresent()){
            throw new ApiException("CODE IS EXIST");
        }
        Product product = adminProductRepository.save(AdminProductMapper.INSTANCE.adminProductRequestToProduct(productDTO));

        return AdminProductMapper.INSTANCE.productToAdminProductResponse(product);
    }

    @Override
    public AdminProductResponse update(AdminProductRequest productDTO) {
        Optional<Product> productOptional = adminProductRepository.findByName(productDTO.getId(), productDTO.getName());
        if (productOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        productOptional = adminProductRepository.findByCode(productDTO.getId(), productDTO.getCode());
        if (productOptional.isPresent()){
            throw new ApiException("CODE IS EXIST");
        }

        productOptional = adminProductRepository.findById(productDTO.getId());
        if (productOptional.isEmpty()){
            throw new ResourceNotFoundException("Product IS NOT EXIST");
        }
        Product productSave = productOptional.get();
        productSave.setName(productDTO.getName());
        productSave.setStatus(productDTO.getStatus());
        productSave.setCode(productDTO.getCode());
        return AdminProductMapper.INSTANCE.productToAdminProductResponse(adminProductRepository.save(productSave));
    }

    @Override
    public AdminProductResponse findById(UUID id) {
        Optional<Product> productOptional = adminProductRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new RuntimeException("LOOI");
        }

        return AdminProductMapper.INSTANCE.productToAdminProductResponse(productOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Product> productOptional = adminProductRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new ResourceNotFoundException("Product NOT FOUND");
        }
        Product product = productOptional.get();
        product.setDeleted(true);
        adminProductRepository.save(product);
        return true;
    }
}
