package com.ndt.be_stepupsneaker.core.client.service.impl.vnpay;

import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.service.vnpay.VNPayService;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.util.VNPayUtil;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {
    private final ClientOrderRepository clientOrderRepository;
    private final ClientPaymentRepository clientPaymentRepository;
    private final ClientPaymentMethodRepository clientPaymentMethodRepository;
    private final ClientProductDetailRepository clientProductDetailRepository;
    private final ClientVoucherRepository clientVoucherRepository;

    @Override
    public String createOrder(int total, String orderInfor) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayUtil.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayUtil.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        vnp_Params.put("vnp_ReturnUrl", VNPayUtil.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(VNPayUtil.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayUtil.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    private int orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayUtil.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    @Override
    public Order authenticateVnPay(HttpServletRequest request) {
        int result = orderReturn(request);
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setTransactionCode(request.getParameter("vnp_TransactionNo"));
        transactionInfo.setOrderInfo(request.getParameter("vnp_OrderInfo"));
        transactionInfo.setPaymentTime(request.getParameter("vnp_PayDate"));
        transactionInfo.setTotalPrice(request.getParameter("vnp_Amount"));
        Optional<Order> orderOptional = clientOrderRepository.findById(transactionInfo.getOrderInfo());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Order Not Found!");
        }
        if (result == 1) {
            PaymentMethod paymentMethod = clientPaymentMethodRepository.findByName("Card")
                    .orElseThrow(() -> new ResourceNotFoundException("Payment Method Not Found !"));
            Payment payment = new Payment();
            payment.setOrder(orderOptional.get());
            payment.setPaymentMethod(paymentMethod);
            payment.setDescription(orderOptional.get().getNote());
            payment.setTotalMoney(orderOptional.get().getTotalMoney());
            payment.setTransactionCode(transactionInfo.getTransactionCode());
            clientPaymentRepository.save(payment);
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.WAIT_FOR_DELIVERY);
            return clientOrderRepository.save(order);
        } else {
            for (OrderDetail orderDetail : orderOptional.get().getOrderDetails()) {
                ProductDetail productDetail = clientProductDetailRepository.findById(orderDetail.getProductDetail().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("ProductDetail Not Found !"));
                productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
                clientProductDetailRepository.save(productDetail);
            }
            Voucher voucher = orderOptional.get().getVoucher();
            if (voucher != null) {
                voucher.setQuantity(voucher.getQuantity() + 1);
                clientVoucherRepository.save(voucher);
            }
        }
        return null;
    }

}
