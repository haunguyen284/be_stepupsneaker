package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminTradeMarkResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminTradeMarkMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminTradeMarkRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminTradeMarkService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.TradeMark;
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
public class AdminTradeMarkServiceImpl implements AdminTradeMarkService {
    @Autowired
    private AdminTradeMarkRepository adminTradeMarkRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminTradeMarkResponse> findAllEntity(AdminTradeMarkRequest soleRequest) {

        Pageable pageable = paginationUtil.pageable(soleRequest);
        Page<TradeMark> resp = adminTradeMarkRepository.findAllTradeMark(soleRequest, soleRequest.getStatus(), pageable);
        Page<AdminTradeMarkResponse> adminTradeMarkResponses = resp.map(AdminTradeMarkMapper.INSTANCE::colorToAdminTradeMarkResponse);
        return new PageableObject<>(adminTradeMarkResponses);
    }

    @Override
    public AdminTradeMarkResponse create(AdminTradeMarkRequest soleRequest) {
        Optional<TradeMark> brandOptional = adminTradeMarkRepository.findByName(soleRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }
        TradeMark sole = adminTradeMarkRepository.save(AdminTradeMarkMapper.INSTANCE.adminTradeMarkRequestToTradeMark(soleRequest));

        return AdminTradeMarkMapper.INSTANCE.colorToAdminTradeMarkResponse(sole);
    }

    @Override
    public AdminTradeMarkResponse update(AdminTradeMarkRequest soleRequest) {
        Optional<TradeMark> brandOptional = adminTradeMarkRepository.findByName(soleRequest.getId(), soleRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        brandOptional = adminTradeMarkRepository.findById(soleRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("TRADEMARK IS NOT EXIST");
        }
        TradeMark soleSave = brandOptional.get();
        soleSave.setName(soleRequest.getName());
        soleSave.setStatus(soleRequest.getStatus());
        return AdminTradeMarkMapper.INSTANCE.colorToAdminTradeMarkResponse(adminTradeMarkRepository.save(soleSave));
    }

    @Override
    public AdminTradeMarkResponse findById(String id) {
        Optional<TradeMark> soleOptional = adminTradeMarkRepository.findById(id);
        if (soleOptional.isEmpty()){
            throw new ResourceNotFoundException("TRADEMARK IS NOT EXIST");
        }

        return AdminTradeMarkMapper.INSTANCE.colorToAdminTradeMarkResponse(soleOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<TradeMark> soleOptional = adminTradeMarkRepository.findById(id);
        if (soleOptional.isEmpty()){
            throw new ResourceNotFoundException("TRADEMARK NOT FOUND");
        }
        TradeMark sole = soleOptional.get();
        sole.setDeleted(true);
        adminTradeMarkRepository.save(sole);
        return true;
    }
}
