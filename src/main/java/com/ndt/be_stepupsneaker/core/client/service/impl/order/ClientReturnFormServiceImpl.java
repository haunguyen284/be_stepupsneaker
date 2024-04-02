package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientReturnFormDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientReturnFormRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientReturnFormResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientAddressMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientReturnFormDetailMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientReturnFormMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientReturnFormRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientReturnFormService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormDetail;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.RefundStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnDeliveryStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormType;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnInspectionStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.OrderUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientReturnFormServiceImpl implements ClientReturnFormService {
    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MySessionInfo mySessionInfo;

    @Autowired
    private ClientReturnFormRepository clientReturnFormRepository;

    @Autowired
    private ClientOrderRepository clientOrderRepository;

    @Autowired
    private ClientPaymentMethodRepository clientPaymentMethodRepository;

    @Autowired
    private ClientOrderDetailRepository clientOrderDetailRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    @Autowired
    private OrderUtil orderUtil;

    @Autowired
    private ClientAddressRepository clientAddressRepository;

    @Autowired
    private AdminReturnFormHistoryRepository adminReturnFormHistoryRepository;

    @Autowired
    private AdminReturnFormDetailRepository adminReturnFormDetailRepository;


    @Override
    public PageableObject<ClientReturnFormResponse> findAllEntity(ClientReturnFormRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        String customerId = mySessionInfo.getCurrentCustomer().getId();
        Page<ReturnForm> resp = clientReturnFormRepository.findAllReturnForm(customerId, pageable);
        Page<ClientReturnFormResponse> adminReturnFormResponses = resp.map(ClientReturnFormMapper.INSTANCE::returnFormToClientReturnFormResponse);
        return new PageableObject<>(adminReturnFormResponses);
    }

    @Override
    public ClientReturnFormResponse findById(String id) {
        String customerId = mySessionInfo.getCurrentCustomer().getId();
        Optional<ReturnForm> optionalReturnForm = clientReturnFormRepository.findByEntityById(id, customerId);
        if (optionalReturnForm.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }
        return ClientReturnFormMapper.INSTANCE.returnFormToClientReturnFormResponse(optionalReturnForm.get());
    }

    @Override
    public ClientReturnFormResponse findByCode(String code) {
        Optional<ReturnForm> optionalReturnForm = clientReturnFormRepository.findEntityByCode(code);
        if (optionalReturnForm.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }
        return ClientReturnFormMapper.INSTANCE.returnFormToClientReturnFormResponse(optionalReturnForm.get());
    }

    @Override
    public ClientReturnFormResponse create(ClientReturnFormRequest request) {
        Optional<Order> orderOptional = clientOrderRepository.findById(request.getOrder());
        if (orderOptional.isEmpty()) {
            throw new ApiException(messageUtil.getMessage("order.notfound"));
        }

        Order order = orderOptional.get();
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_create"));
        }

        Optional<PaymentMethod> paymentMethodOptional = clientPaymentMethodRepository.findByName(request.getPaymentType());
        if (paymentMethodOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound"));
        }

        List<ReturnFormDetail> returnFormDetails = new ArrayList<>();
        for (ClientReturnFormDetailRequest returnFormDetailRequest : request.getReturnFormDetails()) {
            Optional<OrderDetail> orderDetailOptional = clientOrderDetailRepository.findById(returnFormDetailRequest.getOrderDetail());
            if (orderDetailOptional.isEmpty()) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }

            OrderDetail orderDetail = orderDetailOptional.get();
            if (!order.getOrderDetails().contains(orderDetail)) {
                throw new ApiException("order.return.not_match_order_detail");
            }
            ReturnFormDetail returnFormDetail = ClientReturnFormDetailMapper.INSTANCE.clientReturnFormDetailRequestToReturnFormDetail(returnFormDetailRequest);
            returnFormDetail.setOrderDetail(orderDetail);
            returnFormDetail.setUrlImage(cloudinaryUpload.upload(returnFormDetailRequest.getImage()));
            returnFormDetails.add(returnFormDetail);
        }
        order.setStatus(OrderStatus.RETURNED);
        Order orderSave = clientOrderRepository.save(order);
        orderUtil.createOrderHistory(orderSave, OrderStatus.RETURNED, OrderStatus.RETURNED.action_description);


        // save ReturnForm
        ReturnForm returnForm = new ReturnForm();
        Address address = ClientAddressMapper.INSTANCE.clientAddressRequestToAddress(request.getAddress());
        address.setCustomer(null);
        Address addressSave = clientAddressRepository.save(address);

        returnForm.setOrder(orderSave);
        returnForm.setEmployee(null);
        returnForm.setAddress(addressSave);
        returnForm.setType(ReturnFormType.ONLINE);
        returnForm.setPaymentInfo(request.getPaymentInfo());
        returnForm.setPaymentType(request.getPaymentType());

        ReturnForm returnFormSave = clientReturnFormRepository.save(returnForm);

        // save ReturnFormHistory
        ReturnFormHistory returnFormHistory = new ReturnFormHistory();
        returnFormHistory.setReturnForm(returnFormSave);
        returnFormHistory.setNote(ReturnDeliveryStatus.PENDING.action_description);
        returnFormHistory.setActionStatus(ReturnDeliveryStatus.PENDING);
        adminReturnFormHistoryRepository.save(returnFormHistory);

        // save list ReturnFormDetail
        returnFormDetails.stream().map(detail -> {
            detail.setReturnForm(returnFormSave);
            return detail;
        }).collect(Collectors.toList());

        adminReturnFormDetailRepository.saveAll(returnFormDetails);

        return ClientReturnFormMapper.INSTANCE.returnFormToClientReturnFormResponse(clientReturnFormRepository.findById(returnFormSave.getId()).orElseThrow());
    }
}
