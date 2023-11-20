package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminColorResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminColorMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminColorRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminColorService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Color;
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
public class AdminColorServiceImpl implements AdminColorService {
    @Autowired
    private AdminColorRepository adminColorRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminColorResponse> findAllEntity(AdminColorRequest colorRequest) {

        Pageable pageable = paginationUtil.pageable(colorRequest);
        Page<Color> resp = adminColorRepository.findAllColor(colorRequest, colorRequest.getStatus(), pageable);
        Page<AdminColorResponse> adminColorResponses = resp.map(AdminColorMapper.INSTANCE::colorToAdminColorResponse);
        return new PageableObject<>(adminColorResponses);
    }

    @Override
    public AdminColorResponse create(AdminColorRequest colorDTO) {
        Optional<Color> colorOptional = adminColorRepository.findByName(colorDTO.getName());
        if (colorOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        colorOptional = adminColorRepository.findByCode(colorDTO.getCode());
        if (colorOptional.isPresent()){
            throw new ApiException("CODE IS EXIST");
        }
        Color color = adminColorRepository.save(AdminColorMapper.INSTANCE.adminColorRequestToColor(colorDTO));

        return AdminColorMapper.INSTANCE.colorToAdminColorResponse(color);
    }

    @Override
    public AdminColorResponse update(AdminColorRequest colorDTO) {
        Optional<Color> colorOptional = adminColorRepository.findByName(colorDTO.getId(), colorDTO.getName());
        if (colorOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        colorOptional = adminColorRepository.findByCode(colorDTO.getId(), colorDTO.getCode());
        if (colorOptional.isPresent()){
            throw new ApiException("CODE IS EXIST");
        }

        colorOptional = adminColorRepository.findById(colorDTO.getId());
        if (colorOptional.isEmpty()){
            throw new ResourceNotFoundException("COLOR IS NOT EXIST");
        }
        Color colorSave = colorOptional.get();
        colorSave.setName(colorDTO.getName());
        colorSave.setStatus(colorDTO.getStatus());
        colorSave.setCode(colorDTO.getCode());
        return AdminColorMapper.INSTANCE.colorToAdminColorResponse(adminColorRepository.save(colorSave));
    }

    @Override
    public AdminColorResponse findById(String id) {
        Optional<Color> colorOptional = adminColorRepository.findById(id);
        if (colorOptional.isEmpty()){
            throw new RuntimeException("LOOI");
        }

        return AdminColorMapper.INSTANCE.colorToAdminColorResponse(colorOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Color> colorOptional = adminColorRepository.findById(id);
        if (colorOptional.isEmpty()){
            throw new ResourceNotFoundException("COLOR NOT FOUND");
        }
        Color color = colorOptional.get();
        color.setDeleted(true);
        adminColorRepository.save(color);
        return true;
    }
}
