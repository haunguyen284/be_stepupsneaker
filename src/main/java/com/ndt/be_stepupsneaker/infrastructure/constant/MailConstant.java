package com.ndt.be_stepupsneaker.infrastructure.constant;

public class MailConstant {
    public static final String LOGO_PATH = "/static/images/logo.jpg";

    public static final String BODY_STARTS =
            "<div style=\"background-color: #F4F4F4; padding: 20px; text-align: center;\">\n" +
                    "    <img src=\"cid:logoImage\" alt=\"Logo\" width=\"100\" height=\"100\">\n" +
                    "    <h1 style=\"font-size: 24px; color: #333;\">Step Up Sneaker</h1>\n" +
                    "</div>";

//    public static final String BODY_BODY =
//            "<div style=\"background-color: #FFF; padding: 20px; border-radius: 10px; margin: 20px; text-align: left;\">\n" +
//                    "    <h2 style=\"font-size: 20px; color: #333;\">Your Password</h2>\n" +
//                    "    <p style=\"font-size: 16px; color: #666;\">We've detected a new login on your Step Up Sneaker account. Here's your temporary password:</p>\n" +
//                    "    <p style=\"font-size: 18px; color: #000; font-weight: bold;\">{{password}}</p>\n" +
//                    "</div>";
public static final String BODY_BODY =
        "<div style=\"background-color: #FFF; padding: 20px; border-radius: 10px; margin: 20px; text-align: left;\">\n" +
                "    <h2 style=\"font-size: 20px; color: #333;\">Thông tin về đơn hàng của Quý khách</h2>\n" +
                "    <p style=\"font-size: 16px; color: #666;\">Dưới đây là chi tiết về đơn hàng của bạn:</p>\n" +
                "    <table style=\"width: 100%; border-collapse: collapse; font-size: 16px; margin-top: 20px;\">\n" +
                "        <thead>\n" +
                "            <tr style=\"background-color: #F4F4F4;\">\n" +
                "                <th style=\"padding: 10px;\">Mã sản phẩm</th>\n" +
                "                <th style=\"padding: 10px;\">Tên sản phẩm</th>\n" +
                "                <th style=\"padding: 10px;\">Số lượng</th>\n" +
                "                <th style=\"padding: 10px;\">Giá</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "            <!-- Thêm chi tiết đơn hàng tại đây -->\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>";

    public static final String BODY_END =
            "<div style=\"background-color: #F4F4F4; padding: 20px; text-align: center;\">\n" +
                    "    <img src=\"cid:logoImage\" alt=\"Logo\" width=\"100\" height=\"100\">\n" +
                    "    <h1 style=\"font-size: 24px; color: #333;\">Step Up Sneaker</h1>\n" +
                    "    <div style=\"font-size: 14px; color: #666; margin-top: 20px;\">\n" +
                    "        <p>Sent with ❤ from Step Up Sneaker</p>\n" +
                    "        <p>123 Sneaker Street, Shoe City, 56789</p>\n" +
                    "        <p>Phone: (123) 456-7890</p>\n" +
                    "        <p>Email: info@stepupsneaker.com</p>\n" +
                    "        <p>Website: www.stepupsneaker.com</p>\n" +
                    "    </div>\n" +
                    "</div>";
}
