package com.ndt.be_stepupsneaker.infrastructure.email.util;

import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.email.model.Email;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendMailAutoEntity {
    private EmailService emailService;

    @Autowired
    public SendMailAutoEntity(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendMailAutoPassWord(Customer customer, String passWord, Employee employee) {
        String[] toEmail = new String[1];
        Email email = new Email();
        email.setSubject("STEP UP SNEAKER sends you the password ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>STEP UP SNEAKER sends you the password</h1></div>");
        email.setBody("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><div style='border: 2px dotted #000; padding: 10px; display: inline-block;'><p style='font-size: 16px; font-family: Arial, sans-serif;'>Your password : <strong>" + passWord + "</strong></p></div></div>");
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
        email.setSubject("STEP UP SNEAKER sends you the new Voucher ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>STEP UP SNEAKER sends you the new Voucher </h1></div>");
        Voucher voucher = customerVouchers.get(0).getVoucher();
        String valueInfo = voucher.getType() == VoucherType.PERCENTAGE ? voucher.getValue() + "%" : voucher.getValue() + " VNĐ";
        String voucherInfo = "<div style='text-align: center; margin-top: 20px; margin-bottom: 20px; with: 100%'>";
        voucherInfo += "<div style='border: 2px solid #f0f0f0; padding: 20px; display: inline-block; width: 600px; font-family: Arial, sans-serif;'>";
        voucherInfo += "<h2 style='text-align: center; color: #ee4d2d;'>" + voucher.getName() + "</h2>";
        voucherInfo += "<hr style='border-top: 1px solid #f0f0f0;'>";
        voucherInfo += "<img src='" + voucher.getImage() + "' alt='Voucher Image' style='max-width: 100%; height: auto; margin-bottom: 15px;'>";
        voucherInfo += "<table style='width: 100%;'>";
        voucherInfo += "<tr><td><strong>Code:</strong></td><td>" + voucher.getCode() + "</td></tr>";
        voucherInfo += "<tr><td><strong>Value:</strong></td><td>" + valueInfo + "</td></tr>";
        voucherInfo += "<tr><td><strong>Constraint:</strong></td><td>" + voucher.getConstraint() + "</td></tr>";
        voucherInfo += "<tr><td><strong>Start Date:</strong></td><td>" + ConvertUtil.convertLongToLocalDateTime(voucher.getStartDate()) + "</td></tr>";
        voucherInfo += "<tr><td><strong>End Date:</strong></td><td>" + ConvertUtil.convertLongToLocalDateTime(voucher.getEndDate()) + "</td></tr>";
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
        email.setSubject("Your Order Information from STEP UP SNEAKER");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>Your Order Information from STEP UP SNEAKER</h1></div>");
        String emailBody = "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<tr><td colspan='4' style='text-align: center;'><strong>Order Summary</strong></td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td colspan='4'>";
        emailBody += "<table style='width: 100%; border-collapse: collapse; font-family: Arial, sans-serif;'>";
        emailBody += "<thead style='background-color: #f0f0f0;'>";
        emailBody += "<tr>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Product</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Quantity</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Price</th>";
        emailBody += "<th style='border: 1px solid #ddd; padding: 8px;'>Total</th>";
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
        emailBody += "<tr><td colspan='4'><strong>Delivery address     :</strong> "
                + clientOrderResponse.getAddress().getMore() + ", "
                + clientOrderResponse.getAddress().getWardName() + ", "
                + clientOrderResponse.getAddress().getDistrictName() + ", "
                + clientOrderResponse.getAddress().getProvinceName() + "</td></tr>";
        emailBody += "<tr><td colspan='4'><strong>Phone number:</strong> " + clientOrderResponse.getAddress().getPhoneNumber() + "</td></tr>";
        emailBody += "<tr><td colspan='4'>&nbsp;</td></tr>";
        emailBody += "<tr><td><strong>Shipping money :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getShippingMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Voucher       :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(totalVoucher) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Order total   :</strong></td><td colspan='3' style='text-align: right;'><strong>" + ConvertUtil.convertFloatToVnd(clientOrderResponse.getTotalMoney()) + "</strong></td></tr>";
        emailBody += "<tr><td><strong>Order code    :</strong></td><td colspan='3'><a href='" + EntityProperties.URL_FE_TRACKING + clientOrderResponse.getCode() + "'>" + clientOrderResponse.getCode() + "</a></td></tr>";
        emailBody += "<tr><td><strong>Full name     :</strong></td><td colspan='3'>" + clientOrderResponse.getFullName() + "</td></tr>";
        emailBody += "<tr><td><strong>Status        :</strong></td><td colspan='3'>" + clientOrderResponse.getStatus() + "</td></tr>";
        emailBody += "<tr><td><strong>Created at    :</strong></td><td colspan='3'>" + ConvertUtil.convertLongToLocalDateTime(clientOrderResponse.getCreatedAt()) + "</td></tr>";
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
        email.setSubject("STEP UP SNEAKER sends you the forget password ...");
        email.setTitleEmail("<div style='text-align: center; margin-top: 20px; margin-bottom: 20px;'><h1 style='color: red;'>Password Reset</h1></div>");
        String emailBody = "<h3>Reset Your Password</h3>"
                + "<p>To reset your password, please click on the link below:</p>"
                + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p>Thank you!</p>";
        email.setBody(emailBody);
        toEmail[0] = recipientEmail;
        email.setToEmail(toEmail);
        emailService.sendEmail(email);
    }
}
