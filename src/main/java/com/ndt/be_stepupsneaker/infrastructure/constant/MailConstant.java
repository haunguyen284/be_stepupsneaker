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
            "<div class=\"container my-5\">\n" +
                    "  <!-- Footer -->\n" +
                    "  <footer\n" +
                    "          class=\"text-center text-lg-start text-white\"\n" +
                    "          style=\"background-color: #929fba\"\n" +
                    "          >\n" +
                    "    <!-- Grid container -->\n" +
                    "    <div class=\"container p-4 pb-0\">\n" +
                    "      <!-- Section: Links -->\n" +
                    "      <section class=\"\">\n" +
                    "        <!--Grid row-->\n" +
                    "        <div class=\"row\">\n" +
                    "          <!-- Grid column -->\n" +
                    "          <div class=\"col-md-3 col-lg-3 col-xl-3 mx-auto mt-3\">\n" +
                    "            <h6 class=\"text-uppercase mb-4 font-weight-bold\">\n" +
                    "              Company name\n" +
                    "            </h6>\n" +
                    "            <p>\n" +
                    "              Here you can use rows and columns to organize your footer\n" +
                    "              content. Lorem ipsum dolor sit amet, consectetur adipisicing\n" +
                    "              elit.\n" +
                    "            </p>\n" +
                    "          </div>\n" +
                    "          <!-- Grid column -->\n" +
                    "          <hr class=\"w-100 clearfix d-md-none\" />\n" +
                    "          <!-- Grid column -->\n" +
                    "          <div class=\"col-md-2 col-lg-2 col-xl-2 mx-auto mt-3\">\n" +
                    "            <h6 class=\"text-uppercase mb-4 font-weight-bold\">Products</h6>\n" +
                    "            <p>\n" +
                    "              <a class=\"text-white\">MDBootstrap</a>\n" +
                    "            </p>\n" +
                    "            <p>\n" +
                    "              <a class=\"text-white\">MDWordPress</a>\n" +
                    "            </p>\n" +
                    "            <p>\n" +
                    "              <a class=\"text-white\">BrandFlow</a>\n" +
                    "            </p>\n" +
                    "            <p>\n" +
                    "              <a class=\"text-white\">Bootstrap Angular</a>\n" +
                    "            </p>\n" +
                    "          </div>\n" +
                    "          <!-- Grid column -->\n" +
                    "          <hr class=\"w-100 clearfix d-md-none\" />\n" +
                    "          <!-- Grid column -->\n" +
                    "          <div class=\"col-md-4 col-lg-3 col-xl-3 mx-auto mt-3\">\n" +
                    "            <h6 class=\"text-uppercase mb-4 font-weight-bold\">Contact</h6>\n" +
                    "            <p><i class=\"fas fa-home mr-3\"></i> New York, NY 10012, US</p>\n" +
                    "            <p><i class=\"fas fa-envelope mr-3\"></i> info@gmail.com</p>\n" +
                    "            <p><i class=\"fas fa-phone mr-3\"></i> + 01 234 567 88</p>\n" +
                    "            <p><i class=\"fas fa-print mr-3\"></i> + 01 234 567 89</p>\n" +
                    "          </div>\n" +
                    "          <!-- Grid column -->\n" +
                    "          <!-- Grid column -->\n" +
                    "          <div class=\"col-md-3 col-lg-2 col-xl-2 mx-auto mt-3\">\n" +
                    "            <h6 class=\"text-uppercase mb-4 font-weight-bold\">Follow us</h6>\n" +
                    "            <!-- Facebook -->\n" +
                    "            <a\n" +
                    "               class=\"btn btn-primary btn-floating m-1\"\n" +
                    "               style=\"background-color: #3b5998\"\n" +
                    "               href=\"#!\"\n" +
                    "               role=\"button\"\n" +
                    "               ><i class=\"fab fa-facebook-f\"></i\n" +
                    "              ></a>\n" +
                    "            <!-- Twitter -->\n" +
                    "            <a\n" +
                    "               class=\"btn btn-primary btn-floating m-1\"\n" +
                    "               style=\"background-color: #55acee\"\n" +
                    "               href=\"#!\"\n" +
                    "               role=\"button\"\n" +
                    "               ><i class=\"fab fa-twitter\"></i\n" +
                    "              ></a>\n" +
                    "            <!-- Google -->\n" +
                    "            <a\n" +
                    "               class=\"btn btn-primary btn-floating m-1\"\n" +
                    "               style=\"background-color: #dd4b39\"\n" +
                    "               href=\"#!\"\n" +
                    "               role=\" button\"\n" +
                    "               ><i class=\"fab fa-google\"></i\n" +
                    "              ></a>\n" +
                    "            <!-- Instagram -->\n" +
                    "            <a\n" +
                    "               class=\"btn btn primary btn-floating m-1\"\n" +
                    "               style=\"background-color: #ac2bac\"\n" +
                    "               href=\"#!\"\n" +
                    "               role=\"button\"\n" +
                    "               ><i class=\"fab fa-instagram\"></i\n" +
                    "              ></a>\n" +
                    "            <!-- Linkedin -->\n" +
                    "            <a\n" +
                    "               class=\"btn btn-primary btn-floating m-1\"\n" +
                    "               style=\"background-color: #0082ca\"\n" +
                    "               href=\"#!\"\n" +
                    "               role=\"button\"\n" +
                    "               ><i class=\"fab fa-linkedin-in\"></i\n" +
                    "              ></a>\n" +
                    "            <!-- Github -->\n" +
                    "            <a\n" +
                    "               class=\"btn btn-primary btn-floating m-1\"\n" +
                    "               style=\"background-color: #333333\"\n" +
                    "               href=\"#!\"\n" +
                    "               role=\"button\"\n" +
                    "               ><i class=\"fab fa-github\"></i\n" +
                    "              ></a>\n" +
                    "          </div>\n" +
                    "        </div>\n" +
                    "        <!--Grid row-->\n" +
                    "      </section>\n" +
                    "      <!-- Section: Links -->\n" +
                    "    </div>\n" +
                    "    <!-- Grid container -->\n" +
                    "    <!-- Copyright -->\n" +
                    "    <div\n" +
                    "         class=\"text-center p-3\"\n" +
                    "         style=\"background-color: rgba(0, 0, 0, 0.2)\"\n" +
                    "         >\n" +
                    "      © 2020 Copyright:\n" +
                    "      <a class=\"text-white\" href=\"https://mdbootstrap.com/\"\n" +
                    "         >MDBootstrap.com</a\n" +
                    "        >\n" +
                    "    </div>\n" +
                    "    <!-- Copyright -->\n" +
                    "  </footer>\n" +
                    "  <!-- Footer -->\n" +
                    "</div>\n" +
                    "<!-- End of .container -->";
}
