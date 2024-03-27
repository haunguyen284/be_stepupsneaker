package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminReturnFormMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminReturnFormService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
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
public class AdminReturnFormServiceImpl implements AdminReturnFormService {
    @Autowired
    private AdminReturnFormRepository adminReturnFormRepository;

    @Autowired
    private AdminOrderRepository adminOrderRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MessageUtil messageUtil;
    @Override
    public PageableObject<AdminReturnFormResponse> findAllEntity(AdminReturnFormRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<ReturnForm> resp = adminReturnFormRepository.findAllReturnForm(request.getStatus(), pageable);
        Page<AdminReturnFormResponse> adminReturnFormResponses = resp.map(AdminReturnFormMapper.INSTANCE::returnFormToAdminReturnFormResponse);
        return new PageableObject<>(adminReturnFormResponses);
    }

    @Override
    public AdminReturnFormResponse create(AdminReturnFormRequest request) {
        Optional<Order> orderOptional = adminOrderRepository.findByCode(request.getOrderCode());
        if (orderOptional.isEmpty()) {
            throw new ApiException(messageUtil.getMessage("order.notfound"));
        }
        if (orderOptional.get().getStatus() != OrderStatus.COMPLETED){
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_create"));
        }
        ReturnForm returnForm = AdminReturnFormMapper.INSTANCE.adminReturnFormRequestToReturnForm(request);
        returnForm.setOrder(orderOptional.get());
        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(adminReturnFormRepository.save(returnForm));
    }

    @Override
    public AdminReturnFormResponse update(AdminReturnFormRequest request) {
        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(request.getId());
        if (optionalReturnForm.isEmpty()) {
            throw new ApiException(messageUtil.getMessage("return_form.notfound"));
        }
        Optional<Order> orderOptional = adminOrderRepository.findByCode(request.getOrderCode());
        if (orderOptional.isPresent() && orderOptional.get().getStatus() != OrderStatus.COMPLETED){
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_update"));
        }
        ReturnForm returnForm = optionalReturnForm.get();
        returnForm.setReason(request.getReason());
        returnForm.setFeedback(request.getFeedback());
        returnForm.setStatus(request.getStatus());
        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(adminReturnFormRepository.save(returnForm));
    }

    @Override
    public AdminReturnFormResponse findById(String id) {
        return null;
    }


    @Override
    public Boolean delete(String id) {
        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(id);
        if (optionalReturnForm.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }
        ReturnForm returnForm = optionalReturnForm.get();
        returnForm.setDeleted(true);
        adminReturnFormRepository.save(returnForm);
        return true;
    }
}
