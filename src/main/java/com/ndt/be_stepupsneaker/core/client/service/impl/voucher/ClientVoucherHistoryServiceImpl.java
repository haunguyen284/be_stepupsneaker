package com.ndt.be_stepupsneaker.core.client.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.voucher.ClientVoucherHistoryMapper;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.service.voucher.ClientVoucherHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientVoucherHistoryServiceImpl implements ClientVoucherHistoryService {
    @Autowired
    private ClientVoucherHistoryRepository clientVoucherHistoryRepository;

    @Autowired
    private PaginationUtil paginationUtil;
    @Override
    public PageableObject<ClientVoucherHistoryResponse> findAllEntity(ClientVoucherHistoryRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<VoucherHistory> resp = clientVoucherHistoryRepository.findAllVoucherHistory(request, pageable);
        Page<ClientVoucherHistoryResponse> ClientVoucherHistoryResponses = resp.map(ClientVoucherHistoryMapper.INSTANCE::voucherHistoryToClientVoucherHistoryResponse);
        return new PageableObject<>(ClientVoucherHistoryResponses);

    }
}
