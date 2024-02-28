package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductService;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientProductMapper;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Product;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
public class AdminProductServiceImpl implements AdminProductService {
    
    @Autowired
    private CloudinaryUpload cloudinaryUpload;
    
    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public PageableObject<AdminProductResponse> findAllEntity(AdminProductRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        List<AdminProductResponse> adminProductResponses = new ArrayList<>();

        Page<Object[]> resp = adminProductRepository.findAllProduct(request, request.getStatus(), pageable);
        for (Object[] result : resp) {
            Product product = (Product) result[0];
            AdminProductResponse adminProductResponse = AdminProductMapper.INSTANCE.productToAdminProductResponse(product);
            adminProductResponse.setSaleCount((Long) result[1]);
            adminProductResponse.setQuantity((Long) result[2]);
            adminProductResponses.add(adminProductResponse);
        }
        Page<AdminProductResponse> adminProductResponsePage = new PageImpl<>(adminProductResponses, pageable, resp.getTotalElements());

        return new PageableObject<>(adminProductResponsePage);
    }

    @Override
    public Object create(AdminProductRequest productDTO) {
        Optional<Product> productOptional = adminProductRepository.findByName(productDTO.getName());
        if (productOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.name.exist"));
        }

        productOptional = adminProductRepository.findByCode(productDTO.getCode());
        if (productOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.code.exist"));
        }
        productDTO.setImage(cloudinaryUpload.upload(productDTO.getImage()));

        Product product = adminProductRepository.save(AdminProductMapper.INSTANCE.adminProductRequestToProduct(productDTO));

        return AdminProductMapper.INSTANCE.productToAdminProductResponse(product);
    }

    @Override
    public AdminProductResponse update(AdminProductRequest productDTO) {

        Optional<Product> productOptional = adminProductRepository.findByName(productDTO.getId(), productDTO.getName());
        if (productOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.name.exist"));
        }
        productOptional = adminProductRepository.findByCode(productDTO.getId(), productDTO.getCode());
        if (productOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.code.exist"));
        }

        productOptional = adminProductRepository.findById(productDTO.getId());
        if (productOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.notfound"));
        }

        Product productSave = productOptional.get();

        productSave.setName(productDTO.getName());
        productSave.setStatus(productDTO.getStatus());
        productSave.setCode(productDTO.getCode());
        productSave.setDescription(productDTO.getDescription());
        productSave.setImage(productDTO.getImage());
        productSave.setImage(cloudinaryUpload.upload(productDTO.getImage()));
        return AdminProductMapper.INSTANCE.productToAdminProductResponse(adminProductRepository.save(productSave));
    }

    @Override
    public AdminProductResponse findById(String id) {
        Optional<Product> productOptional = adminProductRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new RuntimeException(messageUtil.getMessage("product.notfound"));
        }

        return AdminProductMapper.INSTANCE.productToAdminProductResponse(productOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Product> productOptional = adminProductRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.notfound"));
        }
        Product product = productOptional.get();
        product.setDeleted(true);
        adminProductRepository.save(product);
        return true;
    }
}
