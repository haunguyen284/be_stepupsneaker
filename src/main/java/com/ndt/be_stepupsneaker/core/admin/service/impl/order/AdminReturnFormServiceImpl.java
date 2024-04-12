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
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnInspectionStatus;
import com.ndt.be_stepupsneaker.infrastructure.email.content.EmailSampleContent;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
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

import java.time.Duration;
import java.time.Instant;
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

    @Autowired
    private EmailService emailService;
@Autowired
    private  EmailSampleContent emailSampleContent;

    @Override
    public PageableObject<AdminReturnFormResponse> findAllEntity(AdminReturnFormRequest request) {
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
        String employeeId = "";
        if (!employee.getRole().getName().contains("ADMIN")) {
            employeeId = employee.getId();
        }
        Pageable pageable = paginationUtil.pageable(request);
        Page<ReturnForm> resp = adminReturnFormRepository.findAllReturnForm(request, employeeId, pageable);
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

        Instant createdAtInstant = Instant.ofEpochMilli(order.getCreatedAt());
        Duration duration = Duration.between(createdAtInstant, Instant.now());
        if (duration.toDays() > 7) {
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_create"));
        }

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ApiException(messageUtil.getMessage("return_form.order.cant_create"));
        }

        Optional<PaymentMethod> paymentMethodOptional = adminPaymentMethodRepository.findByName(request.getPaymentType());
        if (paymentMethodOptional.isEmpty()) {
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

            // lưu số lượng trả lại sản phẩm chi tiết
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setReturnQuantity(productDetail.getQuantity() + returnFormDetail.getQuantity());
            adminProductDetailRepository.save(productDetail);

            int remainQuantity = orderDetail.getQuantity() - returnFormDetailRequest.getQuantity();
            if (remainQuantity < 0) {
                throw new ApiException("Bad request");
            } else if (remainQuantity == 0) {
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

        // nếu đơn hàng đã dùng voucher trước đó thì dùng công thức tính lại số tiền cần trả
        float remainingMoney = order.getOriginMoney() - totalMoneyReturn;
        float newReduceMoney = orderUtil.getMoneyReduceForTotalMoney(remainingMoney, order.getCustomer().getId());
        if (order.getReduceMoney() > 0) {
            totalMoneyReturn = totalMoneyReturn - order.getReduceMoney() + newReduceMoney;
        }
        order.setOriginMoney(remainingMoney);
        if (order.getReduceMoney() > 0) {
            order.setReduceMoney(newReduceMoney);
            order.setTotalMoney(remainingMoney - newReduceMoney + order.getShippingMoney());
        } else {
            order.setTotalMoney(remainingMoney + order.getShippingMoney());
        }
        order.setStatus(OrderStatus.RETURNED);
        Order orderSave = adminOrderRepository.save(order);
        orderUtil.createOrderHistory(orderSave, OrderStatus.RETURNED, OrderStatus.RETURNED.action_description);

        // save Payment neu trang thai hoan tien thanh cong
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethodOptional.get());
        payment.setOrder(orderSave);
        payment.setTotalMoney(-totalMoneyReturn);
        if (!Objects.equals(request.getPaymentInfo(), "")) {
            payment.setTransactionCode(request.getPaymentInfo());
        } else {
            payment.setTransactionCode("---");
        }
        adminPaymentRepository.save(payment);

        // save ReturnForm
        ReturnForm returnForm = new ReturnForm();
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));

        returnForm.setOrder(orderSave);
        returnForm.setEmployee(employee);
        returnForm.setAmountToBePaid(totalMoneyReturn);
        returnForm.setEmail(request.getEmail());
        returnForm.setPaymentInfo(request.getPaymentInfo());
        returnForm.setPaymentType(request.getPaymentType());

        ReturnForm returnFormSave = adminReturnFormRepository.save(returnForm);

        // save list ReturnFormDetail
        returnFormDetails.stream().map(detail -> {
            detail.setReturnForm(returnFormSave);
            return detail;
        }).collect(Collectors.toList());

        List<ReturnFormDetail> saveReturnFormDetails = adminReturnFormDetailRepository.saveAll(returnFormDetails);
        ReturnForm returnFormSuccess = adminReturnFormRepository.findById(returnFormSave.getId()).orElseThrow();
        returnFormSuccess.setReturnFormDetails(saveReturnFormDetails);

//        EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
        String subject = "Thông tin phiếu trả hàng của bạn từ Step Up Sneaker";
        emailSampleContent.sendMailAutoReturnOrder(returnFormSuccess, subject);

        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(returnFormSuccess);
    }

    @Override
    @Transactional
    public AdminReturnFormResponse update(AdminReturnFormRequest request) {
        throw new ResourceNotFoundException("API không hỗ trợ");
    }

    @Override
    public AdminReturnFormResponse findById(String id) {
        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(id);
        if (optionalReturnForm.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }
        return AdminReturnFormMapper.INSTANCE.returnFormToAdminReturnFormResponse(optionalReturnForm.get());
    }


    @Override
    public Boolean delete(String id) {
        Optional<ReturnForm> optionalReturnForm = adminReturnFormRepository.findById(id);
        if (optionalReturnForm.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("return_form.notfound"));
        }
        ReturnForm returnForm = optionalReturnForm.get();
        returnForm.setDeleted(true);
        adminReturnFormRepository.save(returnForm);
        return true;
    }

    @Override
    public AdminReturnFormResponse updateReturnDeliveryStatus(AdminReturnFormRequest request) {
        throw new ResourceNotFoundException("API không hỗ trợ");
    }
}
