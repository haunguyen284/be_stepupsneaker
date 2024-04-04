package com.ndt.be_stepupsneaker.infrastructure.email.content;

import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnDeliveryStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormType;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.util.ConvertUtil;
import com.ndt.be_stepupsneaker.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSampleContent {
    private EmailService emailService;

    @Autowired
    public EmailSampleContent(EmailService emailService) {
        this.emailService = emailService;
    }


    public void sendMailAutoPassWord(Customer customer, String passWord, Employee employee) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject("STEP UP SNEAKER gửi mật khẩu cho tài khoản bạn vừa đăng kí ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>STEP UP SNEAKER gửi mật khẩu</h1></div>");
        email.setBody("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><div style='border: 2px dotted #000; padding: 10px; display: inline-block;'><p style='font-size: 16px; font-family: Arial, sans-serif;'>Mật khẩu của bạn : <strong>" + passWord + "</strong></p></div></div>");
        if (customer != null) {
            toEmail[0] = customer.getEmail();
        } else {
            toEmail[0] = employee.getEmail();
        }
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoVoucherToCustomer(List<CustomerVoucher> customerVouchers) {
        Email email = new Email();
        email.setSubject("Bạn có phiếu giảm giá mới từ STEP UP SNEAKER ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>Phiếu giảm giá mới dành cho bạn </h1></div>");
        Voucher voucher = customerVouchers.get(0).getVoucher();
        String valueInfo = voucher.getType() == VoucherType.PERCENTAGE ? voucher.getValue() + "%" : voucher.getValue() + " VNĐ";
        String voucherInfo = "<div style='text-align: center; margin-top: 20px; margin-bottom: 20px; with: 100%'>";
        voucherInfo += "<div style='border: 2px solid #f0f0f0; padding: 20px; display: inline-block; width: 600px; font-family: Arial, sans-serif;'>";
        voucherInfo += "<h2 style='text-align: center; color: #ee4d2d;'>" + voucher.getName() + "</h2>";
        voucherInfo += "<hr style='border-top: 1px solid #f0f0f0;'>";
        voucherInfo += "<img src='" + voucher.getImage() + "' alt='Voucher Image' style='max-width: 100%; height: auto; margin-bottom: 15px;'>";
        voucherInfo += "<table style='width: 100%;'>";
        voucherInfo += "<tr><td><strong>Mã phiếu:</strong></td><td>" + voucher.getCode() + "</td></tr>";
        voucherInfo += "<tr><td><strong>Giá trị:</strong></td><td>" + valueInfo + "</td></tr>";
        voucherInfo += "<tr><td><strong>Điều kiện:</strong></td><td>" + voucher.getConstraint() + "</td></tr>";
        voucherInfo += "<tr><td><strong>Ngày bắt đầu:</strong></td><td>" + ConvertUtil.convertLongToLocalDateTime(voucher.getStartDate()) + "</td></tr>";
        voucherInfo += "<tr><td><strong>Ngày kết thúc:</strong></td><td>" + ConvertUtil.convertLongToLocalDateTime(voucher.getEndDate()) + "</td></tr>";
        voucherInfo += "</table>";
        voucherInfo += "</div></div>";
        email.setBody(voucherInfo);
        String[] toEmail = customerVouchers
                .stream()
                .map(customerVoucher -> customerVoucher.getCustomer().getEmail())
                .toArray(String[]::new);
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoInfoOrderToClient(ClientOrderResponse clientOrderResponse, String emailReq) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject("Thông tin đơn hàng của bạn từ STEP UP SNEAKER");
        String emailTitle = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailTitle += "<tr><td colspan='4' style='text-align: center;'><strong>Thông tin đơn hàng</strong></td></tr>";
        emailTitle += "<tr><td colspan='4'>&nbsp;</td></tr>";
        email.setTitleEmail(emailTitle);
        String emailBody = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'>";
        emailBody += "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<thead style='background-color: #f0f0f0;'>";
        emailBody += "<tr>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tên sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Giá sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tổng tiền</th>";
        emailBody += "</tr>";
        emailBody += "</thead>";
        emailBody += "<tbody>";

        List<ClientOrderDetailResponse> orderDetails = clientOrderResponse.getOrderDetails();
        float totalOrderDetail = 0.0f;
        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (ClientOrderDetailResponse orderDetail : orderDetails) {
                ClientProductDetailResponse productDetail = orderDetail.getProductDetail();
                if (productDetail != null) {
                    float totalPrice = orderDetail.getQuantity() * productDetail.getPrice();
                    totalOrderDetail += totalPrice;
                    emailBody += "<tr>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + productDetail.getProduct().getName() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + orderDetail.getQuantity() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(productDetail.getPrice()) + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(totalPrice) + "</td>";
                    emailBody += "</tr>";
                }
            }
        }
        float totalVoucher = 0.0f;
        if (clientOrderResponse.getVoucher() != null) {
            float discount = clientOrderResponse.getVoucher().getType() == VoucherType.CASH ? clientOrderResponse.getVoucher().getValue() : (clientOrderResponse.getVoucher().getValue() / 100) * totalOrderDetail;
            float finalTotalPrice = Math.max(0, totalOrderDetail - discount);
            totalVoucher = finalTotalPrice;
        }
        emailBody += "</tbody>";
        emailBody += "</table>";
        emailBody += "</td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Họ và tên:</strong> " + clientOrderResponse.getFullName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Địa chỉ giao hàng     :</strong> "
                + clientOrderResponse.getAddress().getMore() + ", "
                + clientOrderResponse.getAddress().getWardName() + ", "
                + clientOrderResponse.getAddress().getDistrictName() + ", "
                + clientOrderResponse.getAddress().getProvinceName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Số điện thoại:</strong> " + clientOrderResponse.getAddress().getPhoneNumber() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><span style='font-size: 12px;margin-right:10px;'>Bấm vào đây để theo dõi đơn hàng của bạn:</span><a href='" + EntityProperties.URL_FE_TRACKING + clientOrderResponse.getCode() + "' style='background-color: #4CAF50; color: white; padding: 5px 10px; text-decoration: none; border-radius: 3px; font-size: 12px;'>Theo dõi</a></td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td><strong>Tiền vận chuyển :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getShippingMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Tiền được giảm       :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(totalVoucher) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Tổng tiền đơn hàng  :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getTotalMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Ngày tạo    :</strong></td><td colspan='3'>" + ConvertUtil.convertLongToLocalDateTime(clientOrderResponse.getCreatedAt()) + "</td></tr>";
        emailBody += "</table>";
        email.setBody(emailBody);
        if (clientOrderResponse.getCustomer() == null) {
            toEmail[0] = emailReq;
        } else {
            toEmail[0] = clientOrderResponse.getCustomer().getEmail();
        }
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoResetPassword(String recipientEmail, String resetLink) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject("STEP UP SNEAKER gửi bạn link thay đổi mật khẩu ...");
        email.setTitleEmail("<h3>Thay đổi mật khẩu</h3>");
        String emailBody = "<p>Để đặt lại mật khẩu của bạn, vui lòng nhấp vào liên kết bên dưới:</p>"
                + "<p><a href=\"" + resetLink + "\">Đặt lại mật khẩu</a></p>"
                + "<p>Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.</p>"
                + "<p>Cảm ơn!</p>";
        email.setBody(emailBody);
        toEmail[0] = recipientEmail;
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoCheckoutVnPay(ClientOrderResponse clientOrderResponse, String emailReq, String urlVnPay) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject("STEP UP SNEAKER thông báo cho bạn vừa tạo đơn hàng thanh toán bằng Vnpay ...");
        String emailTitle = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailTitle += "<tr><td colspan='4' style='text-align: center;'><strong>Thông tin đơn hàng thanh toán bằng VnPay</strong></td></tr>";
        emailTitle += "<tr><td colspan='4'>&nbsp;</td></tr>";
        email.setTitleEmail(emailTitle);
        String emailBody = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'>";
        emailBody += "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<thead style='background-color: #f0f0f0;'>";
        emailBody += "<tr>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tên sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Giá sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tổng tiền</th>";
        emailBody += "</tr>";
        emailBody += "</thead>";
        emailBody += "<tbody>";
        List<ClientOrderDetailResponse> orderDetails = clientOrderResponse.getOrderDetails();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (ClientOrderDetailResponse orderDetail : orderDetails) {
                ClientProductDetailResponse productDetail = orderDetail.getProductDetail();
                if (productDetail != null) {
                    float totalPrice = orderDetail.getQuantity() * productDetail.getPrice();
                    emailBody += "<tr>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + productDetail.getProduct().getName() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + orderDetail.getQuantity() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(productDetail.getPrice()) + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(totalPrice) + "</td>";
                    emailBody += "</tr>";
                }
            }
        }
        emailBody += "</tbody>";
        emailBody += "</table>";
        emailBody += "</td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Họ và tên:</strong> " + clientOrderResponse.getFullName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Địa chỉ giao hàng     :</strong> "
                + clientOrderResponse.getAddress().getMore() + ", "
                + clientOrderResponse.getAddress().getWardName() + ", "
                + clientOrderResponse.getAddress().getDistrictName() + ", "
                + clientOrderResponse.getAddress().getProvinceName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Số điện thoại:</strong> " + clientOrderResponse.getAddress().getPhoneNumber() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><span style='font-size: 12px;margin-right:10px;'>Bấm vào đây để theo dõi đơn hàng của bạn :</span><a href='" + EntityProperties.URL_FE_TRACKING + clientOrderResponse.getCode() + "' style='background-color: #4CAF50; color: white; padding: 5px 10px; text-decoration: none; border-radius: 3px; font-size: 12px;'>Theo dõi</a></td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<p>Nếu bạn gặp sự cố khi thanh toán, vui lòng nhấp vào liên kết bên dưới để thanh toán lại đơn hàng! Liên kết này sẽ có hiệu lực 15 phút kể từ lúc bắt đầu gửi mail.</p>"
                + "<p><a href=\"" + urlVnPay + "\">Thanh toán lại</a></p>"
                + "<p>Nếu bạn không yêu cầu điều này, vui lòng bỏ qua.</p>";
        email.setBody(emailBody);
        emailBody += "<tr><td><strong>Phí vận chuyển :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getShippingMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Tiền được giảm       :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getReduceMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Tổng tiền   :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getTotalMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Ngày tạo   :</strong></td><td colspan='3'>" + ConvertUtil.convertLongToLocalDateTime(clientOrderResponse.getCreatedAt()) + "</td></tr>";
        emailBody += "</table>";
        email.setBody(emailBody);
        if (clientOrderResponse.getCustomer() == null) {
            toEmail[0] = emailReq;
        } else {
            toEmail[0] = clientOrderResponse.getCustomer().getEmail();
        }
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoOrder(Order order, String emailReq, String subject) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject(subject);
        String emailTitle = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailTitle += "<tr><td colspan='4' style='text-align: center;'><strong>Thông tin đơn hàng</strong></td></tr>";
        emailTitle += "<tr><td colspan='4'>&nbsp;</td></tr>";
        email.setTitleEmail(emailTitle);
        String emailBody = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'>";
        emailBody += "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<thead style='background-color: #f0f0f0;'>";
        emailBody += "<tr>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tên sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Giá sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tổng tiền</th>";
        emailBody += "</tr>";
        emailBody += "</thead>";
        emailBody += "<tbody>";

        List<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (OrderDetail orderDetail : orderDetails) {
                ProductDetail productDetail = orderDetail.getProductDetail();
                if (productDetail != null) {
                    emailBody += "<tr>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + productDetail.getProduct().getName() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + orderDetail.getQuantity() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(orderDetail.getPrice()) + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(orderDetail.getTotalPrice()) + "</td>";
                    emailBody += "</tr>";
                }
            }
        }
        emailBody += "</tbody>";
        emailBody += "</table>";
        emailBody += "</td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Họ và tên:</strong> " + order.getFullName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Địa chỉ giao hàng     :</strong> "
                + order.getAddress().getMore() + ", "
                + order.getAddress().getWardName() + ", "
                + order.getAddress().getDistrictName() + ", "
                + order.getAddress().getProvinceName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Số điện thoại:</strong> " + order.getAddress().getPhoneNumber() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><span style='font-size: 12px;margin-right:10px;'>Bấm vào đây để theo dõi đơn hàng của bạn :</span><a href='" + EntityProperties.URL_FE_TRACKING + order.getCode() + "' style='background-color: #4CAF50; color: white; padding: 5px 10px; text-decoration: none; border-radius: 3px; font-size: 12px;'>Theo dõi</a></td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td><strong>Phí vận chuyển :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(order.getShippingMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Tiền được giảm       :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(order.getReduceMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Tổng tiền   :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(order.getTotalMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Ngày thay đổi   :</strong></td><td colspan='3'>" + ConvertUtil.convertLongToLocalDateTime(order.getCreatedAt()) + "</td></tr>";
        emailBody += "</table>";
        email.setBody(emailBody);
        if (order.getCustomer() == null) {
            toEmail[0] = emailReq;
        } else {
            toEmail[0] = order.getCustomer().getEmail();
        }
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }

    public void sendMailAutoReturnOrder(ReturnForm returnForm, String subject) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject(subject);
        String emailTitle = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailTitle += "<tr><td colspan='4' style='text-align: center;'><strong>Thông tin phiếu trả hàng</strong></td></tr>";
        emailTitle += "<tr><td colspan='4'>&nbsp;</td></tr>";
        email.setTitleEmail(emailTitle);
        String emailBody = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'>";
        emailBody += "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<thead style='background-color: #f0f0f0;'>";
        emailBody += "<tr>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Tên sản phẩm</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Giá khi mua</th>";
        emailBody += "</tr>";
        emailBody += "</thead>";
        emailBody += "<tbody>";

        List<ReturnFormDetail> returnFormDetails = returnForm.getReturnFormDetails();
        if (returnFormDetails != null && !returnFormDetails.isEmpty()) {
            for (ReturnFormDetail returnFormDetail : returnFormDetails) {
                OrderDetail orderDetail = returnFormDetail.getOrderDetail();
                ProductDetail productDetail = orderDetail.getProductDetail();
                if (productDetail != null) {
                    emailBody += "<tr>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + productDetail.getProduct().getName() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + returnFormDetail.getQuantity() + "</td>";
                    emailBody += "<td style='border: 1px solid #ddd; padding: 8px;'>" + ConvertUtil.convertFloatToVnd(orderDetail.getPrice()) + "</td>";
                    emailBody += "</tr>";
                }
            }
        }
        String clientInfo = "";
        if (returnForm.getOrder().getCustomer() == null) {
            clientInfo = returnForm.getOrder().getFullName();
        } else {
            clientInfo = returnForm.getOrder().getCustomer().getFullName();
        }

        String phone = "";
        if (returnForm.getOrder().getCustomer() == null) {
            phone = returnForm.getOrder().getPhoneNumber();
        } else {
            phone = returnForm.getAddress().getPhoneNumber();
        }
        String address = returnForm.getAddress().getMore() + ", "
                + returnForm.getAddress().getWardName() + ", "
                + returnForm.getAddress().getDistrictName() + ", "
                + returnForm.getAddress().getProvinceName();

        String returnDeliveryStatus = "";
        if (returnForm.getReturnDeliveryStatus() == ReturnDeliveryStatus.PENDING) {
            returnDeliveryStatus = "Chờ giải quyết";
        } else if (returnForm.getReturnDeliveryStatus() == ReturnDeliveryStatus.RETURNING) {
            returnDeliveryStatus = "Đang được giao đi";
        } else if (returnForm.getReturnDeliveryStatus() == ReturnDeliveryStatus.RECEIVED) {
            returnDeliveryStatus = "Đã nhận được";
        } else {
            returnDeliveryStatus = "Đã hoàn thành";
        }


        emailBody += "</tbody>";
        emailBody += "</table>";
        emailBody += "</td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Họ và tên:</strong> " + clientInfo + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Địa chỉ trả hàng     :</strong> " + address + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Số điện thoại:</strong> " + phone + "</td></tr>";
        emailBody += "<tr><td colspan='4'><span style='font-size: 12px;margin-right:10px;'>Bấm vào đây để theo dõi phiếu trả hàng của bạn :</span><a href='" + EntityProperties.URL_FE_RETURN + returnForm.getCode() + "' style='background-color: #4CAF50; color: white; padding: 5px 10px; text-decoration: none; border-radius: 3px; font-size: 12px;'>Theo dõi</a></td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
//        emailBody += "<tr><td><strong>Hình thức trả hàng :</strong></td><td colspan='3' style='text-align: right;'><strong>" + type + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Trạng thái phiếu trả hàng       :</strong></td><td colspan='3' style='text-align: right;'><strong>" + returnDeliveryStatus + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Thời gian tạo phiếu   :</strong></td><td colspan='3'>" + ConvertUtil.convertLongToLocalDateTime(returnForm.getCreatedAt()) + "</td></tr>";
        emailBody += "</table>";
        email.setBody(emailBody);
        if (returnForm.getEmail() != null) {
            toEmail[0] = returnForm.getEmail();
        } else {
            toEmail[0] = returnForm.getOrder().getCustomer().getEmail();
        }
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }
}
