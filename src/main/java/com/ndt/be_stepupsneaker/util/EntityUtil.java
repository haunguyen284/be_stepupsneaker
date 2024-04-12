package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminCustomerVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionProductDetailRepository;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.content.EmailSampleContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityUtil {
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final AdminPromotionProductDetailRepository adminPromotionProductDetailRepository;
    private final AdminCustomerVoucherRepository adminCustomerVoucherRepository;
    private final AdminCustomerRepository adminCustomerRepository;
    private final EmailSampleContent emailSampleContent;

    public Address updateAddress(Address address, AdminAddressRequest addressRequest) {
        if (addressRequest.getProvinceName() == null || addressRequest.getProvinceName().equals("")) {
            address.setProvinceName(address.getProvinceName());
        } else {
            address.setProvinceName(addressRequest.getProvinceName());
        }
        if (addressRequest.getDistrictName() == null || addressRequest.getDistrictName().equals("")) {
            address.setDistrictName(address.getDistrictName());
        } else {
            address.setDistrictName(addressRequest.getDistrictName());
        }
        if (addressRequest.getWardName() == null || addressRequest.getWardName().equals("")) {
            address.setWardName(address.getWardName());
        } else {
            address.setWardName(addressRequest.getWardName());
        }
        address.setMore(addressRequest.getMore());
        address.setDistrictId(addressRequest.getDistrictId());
        address.setProvinceId(addressRequest.getProvinceId());
        address.setWardCode(addressRequest.getWardCode());
        address.setPhoneNumber(addressRequest.getPhoneNumber());
        return address;
    }

    public List<CustomerVoucher> addCustomersToVoucher(Voucher voucher, List<String> customerIds) {
        List<CustomerVoucher> customerVouchers = new ArrayList<>();
        if (voucher.getCustomerVoucherList() == null) {
            voucher.setCustomerVoucherList(new ArrayList<>());
        }
        if (customerIds != null && !customerIds.isEmpty()) {
            List<Customer> customers = adminCustomerRepository.findAllById(customerIds);
            for (Customer customer : customers) {
                CustomerVoucher customerVoucher = new CustomerVoucher();
                customerVoucher.setCustomer(customer);
                customerVoucher.setVoucher(voucher);
                customerVouchers.add(customerVoucher);
            }
            voucher.setCustomerVoucherList(customerVouchers);
            emailSampleContent.sendMailAutoVoucherToCustomer(customerVouchers);
            return adminCustomerVoucherRepository.saveAll(customerVouchers);

        }

        return null;
    }

    public List<PromotionProductDetail> addProductDetailsToPromotion(Promotion promotion, List<String> productDetailIds) {
        List<PromotionProductDetail> promotionProductDetails = new ArrayList<>();
        if (promotion.getPromotionProductDetailList() == null) {
            promotion.setPromotionProductDetailList(new ArrayList<>());
        }
        if (productDetailIds != null && !productDetailIds.isEmpty()) {
            List<ProductDetail> productDetails = adminProductDetailRepository.findAllById(productDetailIds);
            for (ProductDetail productDetail : productDetails) {
                PromotionProductDetail promotionProductDetail = new PromotionProductDetail();
                promotionProductDetail.setProductDetail(productDetail);
                promotionProductDetail.setPromotion(promotion);
                promotionProductDetails.add(promotionProductDetail);
            }
            return adminPromotionProductDetailRepository.saveAll(promotionProductDetails);
        }
        return null;
    }
}
