package com.ndt.be_stepupsneaker.core.admin.service.impl.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentMethodRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentMethodResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.payment.AdminPaymentMethodMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.admin.service.payment.AdminPaymentMethodService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminPaymentMethodServiceImpl implements AdminPaymentMethodService {

    private final AdminPaymentMethodRepository adminPaymentMethodRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    public AdminPaymentMethodServiceImpl(AdminPaymentMethodRepository adminPaymentMethodRepository, PaginationUtil paginationUtil) {
        this.adminPaymentMethodRepository = adminPaymentMethodRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminPaymentMethodResponse> findAllEntity(AdminPaymentMethodRequest paymentMethodRequest) {
        Pageable pageable = paginationUtil.pageable(paymentMethodRequest);
        Page<PaymentMethod> resp = adminPaymentMethodRepository.findAll(pageable);
        Page<AdminPaymentMethodResponse> adminPaymentMethodResponses = resp.map(AdminPaymentMethodMapper.INSTANCE::paymentToAdminPaymentMethodResponse);
        return new PageableObject<>(adminPaymentMethodResponses);
    }

    @Override
    public Object create(AdminPaymentMethodRequest paymentMethodRequest) {
        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findByName(paymentMethodRequest.getName());
        if(paymentMethodOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        PaymentMethod paymentMethod = adminPaymentMethodRepository.save(AdminPaymentMethodMapper.INSTANCE.adminPaymentMethodRequestToPaymentMethod(paymentMethodRequest));
        return AdminPaymentMethodMapper.INSTANCE.paymentToAdminPaymentMethodResponse(paymentMethod);
    }

    @Override
    public AdminPaymentMethodResponse update(AdminPaymentMethodRequest paymentMethodRequest) {
        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findByName(paymentMethodRequest.getId(), paymentMethodRequest.getName());
        if(paymentMethodOptional.isPresent()){
            throw new ApiException("NAME IS EXIST");
        }

        paymentMethodOptional = adminPaymentMethodRepository.findById(paymentMethodRequest.getId());
        if(paymentMethodOptional.isEmpty()){
            throw new ResourceNotFoundException("PAYMENT METHOD IS NOT EXIST");
        }
        PaymentMethod paymentMethodSave = paymentMethodOptional.get();
        paymentMethodSave.setName(paymentMethodRequest.getName());
        return AdminPaymentMethodMapper.INSTANCE.paymentToAdminPaymentMethodResponse(paymentMethodSave);
    }

    @Override
    public AdminPaymentMethodResponse findById(String id) {
        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findById(id);
        if(paymentMethodOptional.isEmpty()){
            throw new ApiException("PAYMENT METHOD IS NOT EXIST");
        }

        return AdminPaymentMethodMapper.INSTANCE.paymentToAdminPaymentMethodResponse(paymentMethodOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findById(id);
        if(paymentMethodOptional.isEmpty()){
            throw new ApiException("PAYMENT METHOD IS NOT EXIST");
        }
//        PaymentMethod paymentMethod = paymentMethodOptional.get();
//        paymentMethod.setDeleted(true);
        adminPaymentMethodRepository.deleteById(id);
        return true;
    }
}
