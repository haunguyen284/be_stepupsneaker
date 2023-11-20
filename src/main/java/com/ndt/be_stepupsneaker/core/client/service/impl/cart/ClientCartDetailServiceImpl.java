package com.ndt.be_stepupsneaker.core.client.service.impl.cart;

import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartDetailResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.cart.ClientCartDetailMapper;
import com.ndt.be_stepupsneaker.core.client.repository.cart.ClientCartDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.cart.ClientCartRepository;
import com.ndt.be_stepupsneaker.core.client.service.cart.ClientCartDetailService;
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

@Service
@RequiredArgsConstructor
public class ClientCartDetailServiceImpl implements ClientCartDetailService {

    private final ClientCartDetailRepository clientCartDetailRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final ClientCartRepository clientCartRepository;
    private final PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientCartDetailResponse> findAllEntity(ClientCartDetailRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<CartDetail> cartDetailPage = clientCartDetailRepository.findAllCartDetail(request, pageable);
        Page<ClientCartDetailResponse> cartDetailResponses = cartDetailPage.map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse);
        return new PageableObject<>(cartDetailResponses);
    }

    @Override
    public ClientCartDetailResponse create(ClientCartDetailRequest request) {
        ProductDetail productDetail = adminProductDetailRepository.findById(request.getProductDetail())
                .orElseThrow(() -> new ResourceNotFoundException("Product Detail NOT FOUND !"));
        Cart cart = clientCartRepository.findById(request.getCart())
                .orElseThrow(() -> new ResourceNotFoundException("Cart NOT FOUND !"));
        if (request.getQuantity() > productDetail.getQuantity()) {
            throw new ApiException("The quantity of the product you added exceeds the quantity in stock of the product in stock !");
        }
        CartDetail cartDetail = clientCartDetailRepository.findByProductDetailAndCart(productDetail, cart);
        if (cartDetail != null) {
            if (cartDetail.getQuantity() + request.getQuantity() > productDetail.getQuantity()) {
                throw new ApiException("The updated quantity exceeds the available stock for this product !");
            }
            cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
            return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(clientCartDetailRepository.save(cartDetail));
        } else {
            CartDetail newCartDetail = clientCartDetailRepository.save(ClientCartDetailMapper.INSTANCE.clientCartDetailRequestToCartDetail(request));
            return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(newCartDetail);
        }
    }

    @Override
    public ClientCartDetailResponse update(ClientCartDetailRequest request) {
        Optional<CartDetail> optionalCartDetail = clientCartDetailRepository.findById(request.getId());
        if (optionalCartDetail.isEmpty()) {
            throw new ResourceNotFoundException("Cart Detail NOT FOUND !");
        }
        CartDetail cartDetail = optionalCartDetail.get();
        cartDetail.setQuantity(request.getQuantity());
        return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(clientCartDetailRepository.save(cartDetail));
    }

    @Override
    public ClientCartDetailResponse findById(String id) {
        Optional<CartDetail> optionalCartDetail = clientCartDetailRepository.findById(id);
        if (optionalCartDetail.isEmpty()) {
            throw new ResourceNotFoundException("Cart Detail NOT FOUND !");
        }
        return ClientCartDetailMapper.INSTANCE.cartDetailToClientCartDetailResponse(optionalCartDetail.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<CartDetail> optionalCartDetail = clientCartDetailRepository.findById(id);
        if (optionalCartDetail.isEmpty()) {
            throw new ResourceNotFoundException("Cart Detail NOT FOUND !");
        }
        clientCartDetailRepository.delete(optionalCartDetail.get());
        return true;
    }

    @Override
    public Boolean deleteCartDetails(ClientCartDetailRequest cartDetailRequest) {
        clientCartDetailRepository.deleteAllByIdIn(cartDetailRequest.getCartDetails());
        return true;
    }
}
