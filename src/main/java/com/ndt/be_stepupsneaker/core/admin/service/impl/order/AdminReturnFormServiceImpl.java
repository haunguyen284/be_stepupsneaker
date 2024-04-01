package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminCartItemRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminReturnFormDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminReturnFormMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminAddressRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminReturnFormRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminReturnFormService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
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
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormType;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.OrderUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import jakarta.transaction.Transactional;
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
public class AdminReturnFormServiceImpl implements AdminReturnFormService {
    @Autowired
    private AdminReturnFormRepository adminReturnFormRepository;

    @Autowired
    private AdminOrderRepository adminOrderRepository;

    @Autowired
    private AdminAddressRepository adminAddressRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MySessionInfo mySessionInfo;

    @Autowired
    private AdminEmployeeRepository adminEmployeeRepository;

    @Autowired
    private AdminPaymentMethodRepository adminPaymentMethodRepository;

    @Autowired
    private AdminOrderDetailRepository adminOrderDetailRepository;

    @Autowired
    private AdminProductDetailRepository adminProductDetailRepository;

    @Autowired
    private AdminReturnFormDetailRepository adminReturnFormDetailRepository;

    @Autowired
    private AdminPaymentRepository adminPaymentRepository;

    @Autowired
    AdminReturnFormHistoryRepository adminReturnFormHistoryRepository;

    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private OrderUtil orderUtil;

    @Override
    public PageableObject<AdminReturnFormResponse> findAllEntity(AdminReturnFormRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<ReturnForm> resp = adminReturnFormRepository.findAllReturnForm(pageable);
        Page<AdminReturnFormResponse> adminReturnFormResponses = resp.map(AdminReturnFormMapper.INSTANCE::returnFormToAdminReturnFormResponse);
        return new PageableObject<>(adminReturnFormResponses);
    }

    @Override
    @Transactional
    public AdminReturnFormResponse create(AdminReturnFormRequest request) {
        Optional<Order> orderOptional = adminOrderRepository.findById(request.getOrder());
        if (orderOptional.isEmpty()) {
            throw new ApiException(messageUtil.getMessage("order.notfound"));
        }

        Order order = orderOptional.get();
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_create"));
        }

        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findByName(request.getPaymentType());
        if (paymentMethodOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound"));
        }

        float totalMoneyReturn = 0;
        List<ReturnFormDetail> returnFormDetails = new ArrayList<>();
        for (AdminReturnFormDetailRequest returnFormDetailRequest : request.getReturnFormDetails()) {
            Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(returnFormDetailRequest.getOrderDetail());
            if (orderDetailOptional.isEmpty()) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }

            OrderDetail orderDetail = orderDetailOptional.get();
            if (!order.getOrderDetails().contains(orderDetail)) {
                throw new ApiException("order.return.not_match_order_detail");
            }
            ReturnFormDetail returnFormDetail = AdminReturnFormDetailMapper.INSTANCE.adminReturnFormDetailRequestToReturnFormDetail(returnFormDetailRequest);
            returnFormDetail.setOrderDetail(orderDetail);
            returnFormDetail.setUrlImage(cloudinaryUpload.upload(returnFormDetailRequest.getImage()));
            returnFormDetails.add(returnFormDetail);

            // neu trang thai khac pending thi cap nhat so luong sp va hoa don chi tiet
            if (request.getReturnDeliveryStatus() != ReturnDeliveryStatus.PENDING) {
                if (returnFormDetail.isResellable()) {
                    ProductDetail productDetail = orderDetail.getProductDetail();
                    productDetail.setQuantity(productDetail.getQuantity() + returnFormDetail.getQuantity());
                    adminProductDetailRepository.save(productDetail);
                }
                int remainQuantity = orderDetail.getQuantity() - returnFormDetailRequest.getQuantity();
                if (remainQuantity < 0) {
                    throw new ApiException("Bad request");
                } else if (remainQuantity == 0){
                    orderDetail.setStatus(OrderStatus.RETURNED);
                    adminOrderDetailRepository.save(orderDetail);
                    totalMoneyReturn += orderDetail.getTotalPrice();
                } else {
                    orderDetail.setQuantity(remainQuantity);
                    orderDetail.setTotalPrice((remainQuantity) * orderDetail.getPrice());
                    adminOrderDetailRepository.save(orderDetail);
                    OrderDetail orderDetailReturn = new OrderDetail();
                    BeanUtils.copyProperties(orderDetail, orderDetailReturn);
                    orderDetailReturn.setId(null);
                    orderDetailReturn.setStatus(OrderStatus.RETURNED);
                    orderDetailReturn.setQuantity(returnFormDetailRequest.getQuantity());
                    orderDetailReturn.setTotalPrice(returnFormDetailRequest.getQuantity() * orderDetail.getPrice());
                    totalMoneyReturn += returnFormDetailRequest.getQuantity() * orderDetail.getPrice();
                    adminOrderDetailRepository.save(orderDetailReturn);
                }
            }
        }
        Order orderSave = adminOrderRepository.save(order);

        orderUtil.createOrderHistory(orderSave, OrderStatus.RETURNED, OrderStatus.RETURNED.action_description);
        order.setStatus(OrderStatus.RETURNED);
        order.setTotalMoney(order.getTotalMoney() - totalMoneyReturn);

        // save Payment neu trang thai hoan tien thanh cong
        if (request.getRefundStatus() == RefundStatus.COMPLETED) {
            Payment payment = new Payment();
            payment.setPaymentMethod(paymentMethodOptional.get());
            payment.setOrder(orderSave);
            payment.setTotalMoney(-totalMoneyReturn);
            if (!Objects.equals(request.getPaymentInfo(), "")) {
                payment.setTransactionCode(request.getPaymentInfo());
            } else {
                payment.setTransactionCode("CASH");
            }
            adminPaymentRepository.save(payment);
        }

        // save ReturnForm
        ReturnForm returnForm = new ReturnForm();
        Address address = AdminAddressMapper.INSTANCE.adminAddressRequestAddress(request.getAddress());
        address.setCustomer(null);
        Address addressSave = adminAddressRepository.save(address);
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));

        returnForm.setOrder(orderSave);
        returnForm.setEmployee(employee);
        returnForm.setAddress(addressSave);
        returnForm.setAmountToBePaid(totalMoneyReturn);
        returnForm.setType(request.getType());
        returnForm.setPaymentInfo(request.getPaymentInfo());
        returnForm.setPaymentType(request.getPaymentType());
        returnForm.setReturnDeliveryStatus(request.getReturnDeliveryStatus());
        returnForm.setRefundStatus(request.getRefundStatus());

        ReturnForm returnFormSave = adminReturnFormRepository.save(returnForm);

        // save ReturnFormHistory
        ReturnFormHistory returnFormHistory = new ReturnFormHistory();
        returnFormHistory.setReturnForm(returnFormSave);
        returnFormHistory.setNote(request.getReturnDeliveryStatus().action_description);
        returnFormHistory.setActionStatus(request.getReturnDeliveryStatus());
        adminReturnFormHistoryRepository.save(returnFormHistory);

        // save list ReturnFormDetail
        returnFormDetails.stream().map(detail -> {
            detail.setReturnForm(returnFormSave);
                return detail;
            }).collect(Collectors.toList());

        adminReturnFormDetailRepository.saveAll(returnFormDetails);

        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(adminReturnFormRepository.findById(returnFormSave.getId()).orElseThrow());
    }

    @Override
    public AdminReturnFormResponse update(AdminReturnFormRequest request) {
        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(request.getId());
        if (optionalReturnForm.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }

        Optional<Order> orderOptional = adminOrderRepository.findById(request.getOrder());
        if (orderOptional.isEmpty()) {
            throw new ApiException(messageUtil.getMessage("order.notfound"));
        }

        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findByName(request.getPaymentType());
        if (paymentMethodOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound"));
        }
        Order order = orderOptional.get();
        ReturnForm returnForm = optionalReturnForm.get();

        if (returnForm.getReturnDeliveryStatus() != ReturnDeliveryStatus.PENDING) {
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_update"));
        }

        float totalMoneyReturn = 0;
        List<ReturnFormDetail> returnFormDetails = new ArrayList<>();
        for (AdminReturnFormDetailRequest returnFormDetailRequest : request.getReturnFormDetails()) {
            Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(returnFormDetailRequest.getOrderDetail());
            if (orderDetailOptional.isEmpty()) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }

            OrderDetail orderDetail = orderDetailOptional.get();
            if (!order.getOrderDetails().contains(orderDetail)) {
                throw new ApiException("order.return.not_match_order_detail");
            }
            ReturnFormDetail returnFormDetail;
            if (returnFormDetailRequest.getId().contains("splited")){
                returnFormDetail = AdminReturnFormDetailMapper.INSTANCE.adminReturnFormDetailRequestToReturnFormDetail(returnFormDetailRequest);

            } else {
                returnFormDetail = adminReturnFormDetailRepository.findById(returnFormDetailRequest.getId()).orElseThrow(
                        () -> new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"))
                );
            }
            returnFormDetail.setOrderDetail(orderDetail);
            returnFormDetail.setUrlImage(cloudinaryUpload.upload(returnFormDetailRequest.getImage()));
            returnFormDetails.add(returnFormDetail);

            // neu trang thai khac pending thi cap nhat so luong sp va hoa don chi tiet
            if (request.getReturnDeliveryStatus() != ReturnDeliveryStatus.PENDING) {
                if (returnFormDetail.isResellable()) {
                    ProductDetail productDetail = orderDetail.getProductDetail();
                    productDetail.setQuantity(productDetail.getQuantity() + returnFormDetail.getQuantity());
                    adminProductDetailRepository.save(productDetail);
                }
                int remainQuantity = orderDetail.getQuantity() - returnFormDetailRequest.getQuantity();
                if (remainQuantity < 0) {
                    throw new ApiException("Bad request");
                } else if (remainQuantity == 0){
                    orderDetail.setStatus(OrderStatus.RETURNED);
                    adminOrderDetailRepository.save(orderDetail);
                    totalMoneyReturn += orderDetail.getTotalPrice();
                } else {
                    orderDetail.setQuantity(remainQuantity);
                    orderDetail.setTotalPrice((remainQuantity) * orderDetail.getPrice());
                    adminOrderDetailRepository.save(orderDetail);
                    OrderDetail orderDetailReturn = new OrderDetail();
                    BeanUtils.copyProperties(orderDetail, orderDetailReturn);
                    orderDetailReturn.setId(null);
                    orderDetailReturn.setStatus(OrderStatus.RETURNED);
                    orderDetailReturn.setQuantity(returnFormDetailRequest.getQuantity());
                    orderDetailReturn.setTotalPrice(returnFormDetailRequest.getQuantity() * orderDetail.getPrice());
                    totalMoneyReturn += returnFormDetailRequest.getQuantity() * orderDetail.getPrice();
                    adminOrderDetailRepository.save(orderDetailReturn);
                }
            }
        }
        Order orderSave = adminOrderRepository.save(order);

        orderUtil.createOrderHistory(orderSave, OrderStatus.RETURNED, OrderStatus.RETURNED.action_description);
        order.setStatus(OrderStatus.RETURNED);
        order.setTotalMoney(order.getTotalMoney() - totalMoneyReturn);

        // save Payment neu trang thai hoan tien thanh cong
        if (request.getRefundStatus() == RefundStatus.COMPLETED) {
            Payment payment = new Payment();
            payment.setPaymentMethod(paymentMethodOptional.get());
            payment.setOrder(orderSave);
            payment.setTotalMoney(-totalMoneyReturn);
            if (!Objects.equals(request.getPaymentInfo(), "")) {
                payment.setTransactionCode(request.getPaymentInfo());
            } else {
                payment.setTransactionCode("CASH");
            }
            adminPaymentRepository.save(payment);
        }

        // save ReturnForm
        Address address = AdminAddressMapper.INSTANCE.adminAddressRequestAddress(request.getAddress());
        address.setCustomer(null);
        Address addressSave = adminAddressRepository.save(address);
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));

        returnForm.setOrder(orderSave);
        returnForm.setEmployee(employee);
        returnForm.setAddress(addressSave);
        returnForm.setAmountToBePaid(totalMoneyReturn);
        returnForm.setType(request.getType());
        returnForm.setPaymentInfo(request.getPaymentInfo());
        returnForm.setPaymentType(request.getPaymentType());
        returnForm.setReturnDeliveryStatus(request.getReturnDeliveryStatus());
        returnForm.setRefundStatus(request.getRefundStatus());

        ReturnForm returnFormSave = adminReturnFormRepository.save(returnForm);

        // save ReturnFormHistory
        ReturnFormHistory returnFormHistory = new ReturnFormHistory();
        returnFormHistory.setReturnForm(returnFormSave);
        returnFormHistory.setNote(request.getReturnDeliveryStatus().action_description);
        returnFormHistory.setActionStatus(request.getReturnDeliveryStatus());
        adminReturnFormHistoryRepository.save(returnFormHistory);

        // save list ReturnFormDetail
        returnFormDetails.stream().map(detail -> {
            detail.setReturnForm(returnFormSave);
            return detail;
        }).collect(Collectors.toList());

        adminReturnFormDetailRepository.saveAll(returnFormDetails);

        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(adminReturnFormRepository.findById(returnFormSave.getId()).orElseThrow());
    }

    @Override
    public AdminReturnFormResponse findById(String id) {
        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(id);
        if (optionalReturnForm.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }
        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(optionalReturnForm.get());
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

    @Override
    public AdminReturnFormResponse updateReturnDeliveryStatus(AdminReturnFormRequest request) {
//        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(request.getId());
//        if (optionalReturnForm.isEmpty()){
//            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
//        }
//
//        ReturnFormHistory returnFormHistory = new ReturnFormHistory();
//        returnFormHistory.setReturnForm(returnFormSave);
//        returnFormHistory.setNote("Đơn hàng đã được trả");
//        returnFormHistory.setActionStatus(ReturnFormStatus.);
//        adminReturnFormHistoryRepository.save(returnFormHistory);

        return null;
    }
}
