package com.ndt.be_stepupsneaker.core.client.service.impl.voucher;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.voucher.ClientVoucherMapper;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.service.voucher.ClientVoucherService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.scheduled.ScheduledService;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.repository.voucher.CustomerVoucherRepository;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientVoucherServiceImpl implements ClientVoucherService {
    @Qualifier("clientVoucherRepository")
    private final ClientVoucherRepository ClientVoucherRepository;
    private final PaginationUtil paginationUtil;
    private final CustomerVoucherRepository customerVoucherRepository;
    private final ScheduledService scheduledService;
    private final CloudinaryUpload cloudinaryUpload;
    private final MessageUtil messageUtil;
    private final MySessionInfo mySessionInfo;


    @Override
    public PageableObject<ClientVoucherResponse> findAllEntity(ClientVoucherRequest voucherRequest) {
        return null;
    }

    @Override
    public Object create(ClientVoucherRequest voucherRequest) {
        return null;
    }

    @Override
    public ClientVoucherResponse update(ClientVoucherRequest voucherRequest) {
        return null;
    }

    @Override
    public ClientVoucherResponse findById(String id) {
        Optional<Voucher> optionalVoucher = ClientVoucherRepository.findById(id);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
        }

        return ClientVoucherMapper.INSTANCE.voucherToClientVoucherResponse(optionalVoucher.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Voucher> optionalVoucher = ClientVoucherRepository.findById(id);
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
        }
        Voucher newVoucher = optionalVoucher.get();
        newVoucher.setDeleted(true);
        ClientVoucherRepository.save(newVoucher);
        return true;
    }


    @Override
    public PageableObject<ClientVoucherResponse> findAllVoucher(ClientVoucherRequest voucherReq, String customerId) {
        Pageable pageable = paginationUtil.pageable(voucherReq);
        Page<Voucher> resp = ClientVoucherRepository.findAllVoucher(voucherReq, pageable, voucherReq.getStatus(), voucherReq.getType(), customerId);
        Page<ClientVoucherResponse> ClientVoucherResponsePage = resp.map(ClientVoucherMapper.INSTANCE::voucherToClientVoucherResponse);
        return new PageableObject<>(ClientVoucherResponsePage);
    }

    @Override
    public PageableObject<ClientVoucherResponse> findLegitVouchers(String customerId, float totalMoney) {
        ClientVoucherRequest request = new ClientVoucherRequest();
        request.setPageSize(1000);
        Pageable pageable = paginationUtil.pageable(request);
        Page<Voucher> voucherPage = ClientVoucherRepository.findLegitVouchers(pageable, customerId, totalMoney);
        Page<ClientVoucherResponse> clientVoucherResponses = voucherPage.map(ClientVoucherMapper.INSTANCE::voucherToClientVoucherResponse);
        return new PageableObject<>(clientVoucherResponses);
    }

    @Override
    public ClientVoucherResponse findByCode(String code) {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        Optional<Voucher> optionalVoucher = ClientVoucherRepository.findByCode(code, customerResponse.getId());
        if (optionalVoucher.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound"));
        }

        return ClientVoucherMapper.INSTANCE.voucherToClientVoucherResponse(optionalVoucher.get());
    }

}
