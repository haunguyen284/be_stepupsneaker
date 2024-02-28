package com.ndt.be_stepupsneaker.core.admin.service.impl.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.payment.AdminPaymentMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentRepository;
import com.ndt.be_stepupsneaker.core.admin.service.payment.AdminPaymentService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final AdminPaymentRepository adminPaymentRepository;
    private final AdminPaymentMethodRepository adminPaymentMethodRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    public AdminPaymentServiceImpl(
            AdminPaymentRepository adminPaymentRepository,
            AdminPaymentMethodRepository adminPaymentMethodRepository,
            AdminOrderRepository adminOrderRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminPaymentRepository = adminPaymentRepository;
        this.adminPaymentMethodRepository = adminPaymentMethodRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminPaymentResponse> findAllEntity(AdminPaymentRequest paymentRequest) {
        Pageable pageable = paginationUtil.pageable(paymentRequest);
        Page<Payment> resp = adminPaymentRepository.findAll(pageable);
        Page<AdminPaymentResponse> adminPaymentResponses = resp.map(AdminPaymentMapper.INSTANCE::paymentToAdminPaymentResponse);
        return new PageableObject<>(adminPaymentResponses);
    }

    @Override
    public Object create(AdminPaymentRequest paymentRequest) {
        Optional<Payment> paymentOptional = adminPaymentRepository.findByTransactionCode(paymentRequest.getTransactionCode());
        if (paymentOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("payment.transaction_code.exist"));
        }

        Payment payment = adminPaymentRepository.save(AdminPaymentMapper.INSTANCE.adminPaymentRequestToPayment(paymentRequest));
        return AdminPaymentMapper.INSTANCE.paymentToAdminPaymentResponse(payment);
    }

    @Override
    public AdminPaymentResponse update(AdminPaymentRequest paymentRequest) {
        Optional<Payment> paymentOptional = adminPaymentRepository.findByTransactionCode(paymentRequest.getId(), paymentRequest.getTransactionCode());
        if (paymentOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("payment.transaction_code.exist"));
        }

        paymentOptional = adminPaymentRepository.findById(paymentRequest.getId());
        if (paymentOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.notfound"));
        }

        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findById(paymentRequest.getPaymentMethod());
        if (paymentMethodOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound"));
        }

        Optional<Order> orderOptional = adminOrderRepository.findById(paymentRequest.getOrder());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }

        Payment paymentSave = paymentOptional.get();
        paymentSave.setPaymentMethod(paymentMethodOptional.get());
        paymentSave.setOrder(orderOptional.get());
        paymentSave.setTransactionCode(paymentRequest.getTransactionCode());
        paymentSave.setTotalMoney(paymentRequest.getTotalMoney());
        paymentSave.setDescription(paymentRequest.getDescription());
        return AdminPaymentMapper.INSTANCE.paymentToAdminPaymentResponse(paymentSave);
    }

    @Override
    public AdminPaymentResponse findById(String id) {
        Optional<Payment> paymentOptional = adminPaymentRepository.findById(id);
        if (paymentOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.notfound"));
        }
        return AdminPaymentMapper.INSTANCE.paymentToAdminPaymentResponse(paymentOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Payment> paymentOptional = adminPaymentRepository.findById(id);
        if (paymentOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.notfound"));
        }
        Payment payment = paymentOptional.get();
        payment.setDeleted(true);
        adminPaymentRepository.save(payment);
        return true;
    }

    @Override
    public List<AdminPaymentResponse> create(List<AdminPaymentRequest> paymentRequests) {
        List<Payment> payments = paymentRequests.stream().map(AdminPaymentMapper.INSTANCE::adminPaymentRequestToPayment).collect(Collectors.toList());
        return adminPaymentRepository.saveAll(payments).stream().map(AdminPaymentMapper.INSTANCE::paymentToAdminPaymentResponse).collect(Collectors.toList());
    }

    @Override
    public List<AdminPaymentResponse> update(List<AdminPaymentRequest> paymentRequests) {
        return null;
    }
}
