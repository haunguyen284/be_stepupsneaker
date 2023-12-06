package com.ndt.be_stepupsneaker.core.client.service.impl.cart;

import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.cart.ClientCartDetailMapper;
import com.ndt.be_stepupsneaker.core.client.repository.cart.ClientCartDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.cart.ClientCartRepository;
import com.ndt.be_stepupsneaker.core.client.service.cart.ClientCartDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientCartDetailServiceImpl implements ClientCartDetailService {

    private final ClientCartDetailRepository clientCartDetailRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final ClientCartRepository clientCartRepository;
    private final PaginationUtil paginationUtil;
    private final MySessionInfo mySessionInfo;

    @Override
    public PageableObject<ClientCartDetailResponse> findAllEntity(ClientCartDetailRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<CartDetail> cartDetailPage = clientCartDetailRepository.findAllCartDetail(request, cart(), pageable);
        Page<ClientCartDetailResponse> cartDetailResponses = cartDetailPage.map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse);
        return new PageableObject<>(cartDetailResponses);
    }

    @Override
    public Object create(ClientCartDetailRequest request) {
        ProductDetail productDetail = adminProductDetailRepository.findById(request.getProductDetail())
                .orElseThrow(() -> new ResourceNotFoundException("Product Detail NOT FOUND !"));
        if (request.getQuantity() > productDetail.getQuantity()) {
            throw new ApiException("The quantity of the product you added exceeds the quantity in stock of the product in stock !");
        }
        CartDetail cartDetail = clientCartDetailRepository.findByProductDetailAndCart(productDetail, cart());
        if (cartDetail != null) {
            if (cartDetail.getQuantity() + request.getQuantity() > productDetail.getQuantity()) {
                throw new ApiException("The updated quantity exceeds the available stock for this product !");
            }
            cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
            return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(clientCartDetailRepository.save(cartDetail));
        } else {
            CartDetail newCartDetail = ClientCartDetailMapper.INSTANCE.clientCartDetailRequestToCartDetail(request);
            newCartDetail.setCart(cart());
            newCartDetail.setProductDetail(productDetail);
            return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(clientCartDetailRepository.save(newCartDetail));
        }
    }

    @Override
    public ClientCartDetailResponse update(ClientCartDetailRequest request) {
        CartDetail cartDetail = clientCartDetailRepository.findByIdAndCart(request.getId(), cart());
        if (cartDetail == null) {
            throw new ResourceNotFoundException("Cart Detail" + EntityProperties.NOT_FOUND);
        }
        ProductDetail productDetail = cartDetail.getProductDetail();
        if (request.getQuantity() + cartDetail.getQuantity() > productDetail.getQuantity()) {
            throw new ApiException("The updated quantity exceeds the available stock for this product !");
        }
        cartDetail.setQuantity(request.getQuantity());
        return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(clientCartDetailRepository.save(cartDetail));
    }

    @Override
    public ClientCartDetailResponse findById(String id) {
        CartDetail cartDetail = clientCartDetailRepository.findByIdAndCart(id, cart());
        if (cartDetail == null) {
            throw new ResourceNotFoundException("Cart Detail" + EntityProperties.NOT_FOUND);
        }
        return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(cartDetail);
    }

    @Override
    public Boolean delete(String id) {
        CartDetail cartDetail = clientCartDetailRepository.findByIdAndCart(id, cart());
        if (cartDetail == null) {
            throw new ResourceNotFoundException("Cart Detail" + EntityProperties.NOT_FOUND);
        }
        clientCartDetailRepository.delete(cartDetail);
        return true;
    }

    @Override
    public Boolean deleteCartDetails(ClientCartDetailRequest cartDetailRequest) {
        clientCartDetailRepository.deleteAllByIdInAndCart(cartDetailRequest.getCartDetails(), cart());
        return true;
    }

    private Cart cart() {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        Cart cart = clientCartRepository.findById(customerResponse.getCart().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart" + EntityProperties.NOT_FOUND));
        return cart;
    }
}
