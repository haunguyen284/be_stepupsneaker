package com.ndt.be_stepupsneaker.core.admin.service.impl.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.cart.AdminCartDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.cart.AdminCartDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.cart.AdminCartRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.service.cart.AdminCartDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminCartDetailServiceImpl implements AdminCartDetailService {
    private final AdminCartDetailRepository adminCartDetailRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final AdminCartRepository adminCartRepository;
    private final PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminCartDetailResponse> findAllEntity(AdminCartDetailRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<CartDetail> cartDetailPage = adminCartDetailRepository.findAllCartDetail(request, pageable);
        Page<AdminCartDetailResponse> cartDetailResponses = cartDetailPage.map(AdminCartDetailMapper.INSTANCE::cartDetailToAdminCartDetailResponse);
        return new PageableObject<>(cartDetailResponses);
    }

    @Override
    public AdminCartDetailResponse create(AdminCartDetailRequest request) {
        ProductDetail productDetail = adminProductDetailRepository.findById(request.getProductDetail())
                .orElseThrow(() -> new ResourceNotFoundException("Product Detail NOT FOUND !"));
        Cart cart = adminCartRepository.findById(request.getCart())
                .orElseThrow(() -> new ResourceNotFoundException("Cart NOT FOUND !"));
        if (request.getQuantity() > productDetail.getQuantity()) {
            throw new ApiException("The quantity of the product you added exceeds the quantity in stock of the product in stock !");
        }
        CartDetail cartDetail = adminCartDetailRepository.findByProductDetailAndCart(productDetail, cart);
        if (cartDetail != null) {
            if (cartDetail.getQuantity() + request.getQuantity() > productDetail.getQuantity()) {
                throw new ApiException("The updated quantity exceeds the available stock for this product !");
            }
            cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
            return AdminCartDetailMapper.INSTANCE.cartDetailToAdminCartDetailResponse(adminCartDetailRepository.save(cartDetail));
        } else {
            CartDetail newCartDetail = adminCartDetailRepository.save(AdminCartDetailMapper.INSTANCE.adminCartDetailRequestToCartDetail(request));
            return AdminCartDetailMapper.INSTANCE.cartDetailToAdminCartDetailResponse(newCartDetail);
        }
    }

    @Override
    public AdminCartDetailResponse update(AdminCartDetailRequest request) {
        Optional<CartDetail> optionalCartDetail = adminCartDetailRepository.findById(request.getId());
        if (optionalCartDetail.isEmpty()) {
            throw new ResourceNotFoundException("Cart Detail NOT FOUND !");
        }
        CartDetail cartDetail = optionalCartDetail.get();
        ProductDetail productDetail = adminProductDetailRepository.findById(cartDetail.getProductDetail().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductDetail NOT FOUND !"));
        if (cartDetail.getQuantity() + request.getQuantity() > productDetail.getQuantity()) {
            throw new ApiException("The updated quantity exceeds the available stock for this product !");
        }
        cartDetail.setQuantity(request.getQuantity());
        return AdminCartDetailMapper.INSTANCE.cartDetailToAdminCartDetailResponse(adminCartDetailRepository.save(cartDetail));
    }

    @Override
    public AdminCartDetailResponse findById(UUID id) {
        Optional<CartDetail> optionalCartDetail = adminCartDetailRepository.findById(id);
        if (optionalCartDetail.isEmpty()) {
            throw new ResourceNotFoundException("Cart Detail NOT FOUND !");
        }
        return AdminCartDetailMapper.INSTANCE.cartDetailToAdminCartDetailResponse(optionalCartDetail.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<CartDetail> optionalCartDetail = adminCartDetailRepository.findById(id);
        if (optionalCartDetail.isEmpty()) {
            throw new ResourceNotFoundException("Cart Detail NOT FOUND !");
        }
        adminCartDetailRepository.delete(optionalCartDetail.get());
        return true;
    }

    @Override
    public Boolean deleteCartDetails(AdminCartDetailRequest cartDetailRequest) {
        adminCartDetailRepository.deleteAllByIdIn(cartDetailRequest.getCartDetails());
        return true;
    }
}
