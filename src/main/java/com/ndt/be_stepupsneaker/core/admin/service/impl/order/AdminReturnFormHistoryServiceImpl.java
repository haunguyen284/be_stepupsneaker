package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminReturnFormHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminBrandMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminReturnFormHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminReturnFormHistoryServiceImpl implements AdminReturnFormHistoryService {
    @Autowired
    private AdminReturnFormHistoryRepository adminReturnFormHistoryRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminReturnFormHistoryResponse> findAllEntity(AdminReturnFormRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<ReturnFormHistory> resp = adminReturnFormHistoryRepository.findAllEntity(request.getId(), pageable);
        Page<AdminReturnFormHistoryResponse> adminBrandResponses = resp.map(AdminReturnFormHistoryMapper.INSTANCE::returnFormHistoryToAdminReturnFormHistoryResponse);
        return new PageableObject<>(adminBrandResponses);
    }
}
