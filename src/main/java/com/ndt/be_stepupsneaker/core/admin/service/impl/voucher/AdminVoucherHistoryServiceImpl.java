package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminBrandMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminVoucherHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminVoucherHistoryServiceImpl implements AdminVoucherHistoryService {
    private final AdminVoucherHistoryRepository adminVoucherHistoryRepository;
    private final PaginationUtil paginationUtil;

    public PageableObject<AdminVoucherHistoryResponse> findAllEntity(AdminVoucherHistoryRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<VoucherHistory> resp = adminVoucherHistoryRepository.findAllVoucherHistory(request, pageable);
        Page<AdminVoucherHistoryResponse> adminVoucherHistoryResponses = resp.map(AdminVoucherHistoryMapper.INSTANCE::voucherHistoryToAdminVoucherHistoryResponse);
        return new PageableObject<>(adminVoucherHistoryResponses);

    }
}
