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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Cart thisSessionCart = cart();

        if (request.getQuantity() > 5) {
            throw new ApiException("You can only add a maximum of 5 products to your cart!");
        }
        ProductDetail productDetail = adminProductDetailRepository.findById(request.getProductDetail())
                .orElseThrow(() -> new ResourceNotFoundException("Product Detail not found"));

        int requestedQuantity = request.getQuantity();
        int availableQuantity = productDetail.getQuantity();

        validateRequestedQuantity(requestedQuantity, availableQuantity);

        CartDetail cartDetail = clientCartDetailRepository.findByProductDetailAndCart(productDetail, thisSessionCart);

        if (cartDetail != null) {
            validateRequestedQuantity(cartDetail.getQuantity() + requestedQuantity, availableQuantity);

            cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
            clientCartDetailRepository.save(cartDetail);
        } else {
            CartDetail newCartDetail = ClientCartDetailMapper.INSTANCE.clientCartDetailRequestToCartDetail(request);
            newCartDetail.setCart(thisSessionCart);
            newCartDetail.setProductDetail(productDetail);
            newCartDetail.setQuantity(request.getQuantity() != 0 ? request.getQuantity() : 1);
            clientCartDetailRepository.save(newCartDetail);
        }

        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    @Override
    public ClientCartDetailResponse update(ClientCartDetailRequest request) {
        return null;
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
        return null;
    }

    @Override
    public Boolean deleteCartDetails(ClientCartDetailRequest cartDetailRequest) {
        return null;
    }

    @Override
    public List<ClientCartDetailResponse> merge(List<ClientCartDetailRequest> cartDetailRequests) {
        Cart thisSessionCart = cart();

        List<CartDetail> oldCartDetails = clientCartDetailRepository.findAllByCart(thisSessionCart);
        List<CartDetail> newCartDetails = cartDetailRequests.stream()
                .map(ClientCartDetailMapper.INSTANCE::clientCartDetailRequestToCartDetail)
                .filter(this::isValidCartDetail)
                .peek(detail -> {
                    detail.setCart(thisSessionCart);
                    detail.setProductDetail(adminProductDetailRepository.findById(detail.getProductDetail().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("It never runs here lol")));
                })
                .collect(Collectors.toList());

        Map<String, CartDetail> mergedMap = Stream.concat(newCartDetails.stream(), oldCartDetails.stream())
                .collect(Collectors.toMap(
                        cartDetail -> cartDetail.getProductDetail().getId(),
                        Function.identity(),
                        this::mergeCartDetails
                ));

        List<CartDetail> mergedList = new ArrayList<>(mergedMap.values());

        clientCartDetailRepository.deleteAllByCart(thisSessionCart);

        clientCartDetailRepository.saveAll(mergedList);

        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    @Override
    public List<ClientCartDetailResponse> findAll() {
        Cart thisSessionCart = cart();
        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    @Override
    public Object updateQuantity(ClientCartDetailRequest request) {
        Cart thisSessionCart = cart();

        CartDetail cartDetail = clientCartDetailRepository.findByIdAndCart(request.getId(), thisSessionCart);

        if (cartDetail == null) {
            throw new ResourceNotFoundException("Cart Detail" + EntityProperties.NOT_FOUND);
        }

        ProductDetail productDetail = adminProductDetailRepository.findById(request.getProductDetail())
                .orElseThrow(() -> new ResourceNotFoundException("Product Detail not found"));

        int requestedQuantity = request.getQuantity();
        int availableQuantity = productDetail.getQuantity();
        if (requestedQuantity > 5) {
            throw new ApiException("You can only add a maximum of 5 products to your cart!");
        }

        validateRequestedQuantity(requestedQuantity, availableQuantity);

        cartDetail.setQuantity(request.getQuantity());
        clientCartDetailRepository.save(cartDetail);

        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    @Override
    public Object decreaseQuantity(ClientCartDetailRequest request) {
        Cart thisSessionCart = cart();

        CartDetail cartDetail = clientCartDetailRepository.findByIdAndCart(request.getId(), thisSessionCart);

        if (cartDetail == null) {
            throw new ResourceNotFoundException("Cart Detail" + EntityProperties.NOT_FOUND);
        }

        if (request.getQuantity() == 1) {
            clientCartDetailRepository.delete(cartDetail);
        } else {
            cartDetail.setQuantity(cartDetail.getQuantity() - 1);
            clientCartDetailRepository.save(cartDetail);
        }

        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    @Override
    public Object deleteFromCart(String id) {
        Cart thisSessionCart = cart();

        CartDetail cartDetail = clientCartDetailRepository.findByIdAndCart(id, thisSessionCart);

        if (cartDetail == null) {
            throw new ResourceNotFoundException("Cart Detail" + EntityProperties.NOT_FOUND);
        }

        clientCartDetailRepository.delete(cartDetail);

        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    @Override
    public Object deleteAllFromCart() {
        Cart thisSessionCart = cart();

        clientCartDetailRepository.deleteAllByCart(thisSessionCart);

        return clientCartDetailRepository.findAllByCart(thisSessionCart).stream().map(ClientCartDetailMapper.INSTANCE::cartDetailToClientCartDetailResponse).collect(Collectors.toList());
    }

    private CartDetail mergeCartDetails(CartDetail existing, CartDetail replacement) {
        CartDetail mergedCart = new CartDetail();
        mergedCart.setId(existing.getId());
        mergedCart.setCart(existing.getCart());
        mergedCart.setProductDetail(existing.getProductDetail());

        int requestedQuantity = (Math.max(existing.getQuantity(), replacement.getQuantity()));
        int availableQuantity = existing.getProductDetail().getQuantity();

        mergedCart.setQuantity(Math.min(requestedQuantity, availableQuantity));

        mergedCart.setDeleted(existing.getDeleted());
        mergedCart.setCreatedAt(existing.getCreatedAt());
        mergedCart.setCreatedBy(existing.getCreatedBy());
        mergedCart.setUpdatedAt(existing.getUpdatedAt());
        mergedCart.setUpdatedBy(existing.getUpdatedBy());
        return mergedCart;
    }

    private boolean isValidCartDetail(CartDetail cartDetail) {
        Optional<ProductDetail> productDetailOptional = adminProductDetailRepository.findById(cartDetail.getProductDetail().getId());

        return productDetailOptional.isPresent() && cartDetail.getQuantity() <= productDetailOptional.get().getQuantity();
    }

    private void validateRequestedQuantity(int requestedQuantity, int availableQuantity) {
        if (requestedQuantity > availableQuantity) {
            throw new ApiException("The requested quantity exceeds the available stock for the product!");
        }
    }

    private Cart cart() {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        return clientCartRepository.findById(customerResponse.getCart().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart" + EntityProperties.NOT_FOUND));
    }
}
