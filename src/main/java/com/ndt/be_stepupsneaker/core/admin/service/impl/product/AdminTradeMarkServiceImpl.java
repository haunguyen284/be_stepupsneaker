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
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AdminTradeMarkServiceImpl implements AdminTradeMarkService {
    @Autowired
    private AdminTradeMarkRepository adminTradeMarkRepository;

    @Autowired
    private PaginationUtil paginationUtil;
    
    @Autowired
    private MessageUtil messageUtil;

    @Override
    public PageableObject<AdminTradeMarkResponse> findAllEntity(AdminTradeMarkRequest tradeMarkRequest) {

        Pageable pageable = paginationUtil.pageable(tradeMarkRequest);
        Page<TradeMark> resp = adminTradeMarkRepository.findAllTradeMark(tradeMarkRequest, tradeMarkRequest.getStatus(), pageable);
        Page<AdminTradeMarkResponse> adminTradeMarkResponses = resp.map(AdminTradeMarkMapper.INSTANCE::colorToAdminTradeMarkResponse);
        return new PageableObject<>(adminTradeMarkResponses);
    }

    @Override
    public Object create(AdminTradeMarkRequest tradeMarkRequest) {
        Optional<TradeMark> brandOptional = adminTradeMarkRepository.findByName(tradeMarkRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.trade_mark.name.exist"));
        }
        TradeMark tradeMark = adminTradeMarkRepository.save(AdminTradeMarkMapper.INSTANCE.adminTradeMarkRequestToTradeMark(tradeMarkRequest));

        return AdminTradeMarkMapper.INSTANCE.colorToAdminTradeMarkResponse(tradeMark);
    }

    @Override
    public AdminTradeMarkResponse update(AdminTradeMarkRequest tradeMarkRequest) {
        Optional<TradeMark> brandOptional = adminTradeMarkRepository.findByName(tradeMarkRequest.getId(), tradeMarkRequest.getName());
        if (brandOptional.isPresent()){
            throw new ApiException(messageUtil.getMessage("product.trade_mark.name.exist"));
        }

        brandOptional = adminTradeMarkRepository.findById(tradeMarkRequest.getId());
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.trade_mark.notfound"));
        }
        TradeMark tradeMarkSave = brandOptional.get();
        tradeMarkSave.setName(tradeMarkRequest.getName());
        tradeMarkSave.setStatus(tradeMarkRequest.getStatus());
        return AdminTradeMarkMapper.INSTANCE.colorToAdminTradeMarkResponse(adminTradeMarkRepository.save(tradeMarkSave));
    }

    @Override
    public AdminTradeMarkResponse findById(String id) {
        Optional<TradeMark> tradeMarkOptional = adminTradeMarkRepository.findById(id);
        if (tradeMarkOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.trade_mark.notfound"));
        }

        return AdminTradeMarkMapper.INSTANCE.colorToAdminTradeMarkResponse(tradeMarkOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<TradeMark> tradeMarkOptional = adminTradeMarkRepository.findById(id);
        if (tradeMarkOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.trade_mark.notfound"));
        }
        TradeMark tradeMark = tradeMarkOptional.get();
        tradeMark.setDeleted(true);
        adminTradeMarkRepository.save(tradeMark);
        return true;
    }
}
