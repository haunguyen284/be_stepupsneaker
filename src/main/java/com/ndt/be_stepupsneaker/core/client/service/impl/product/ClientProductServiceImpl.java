package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminBrandRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSizeQuantityResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientVariantResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientProductMapper;
import com.ndt.be_stepupsneaker.core.client.repositoty.product.ClientProductRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientProductService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.entity.product.Product;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {
    private final ClientProductRepository clientProductRepository;
    private final PaginationUtil paginationUtil;
    private final AdminBrandRepository adminBrandRepository;


    @Override
    public PageableObject<ClientProductResponse> findAllEntity(ClientProductRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<Product> resp = clientProductRepository.findAllProduct(request, request.getStatus(), pageable);
        Page<ClientProductResponse> adminProductResponses = resp.map(ClientProductMapper.INSTANCE::productToClientProductResponse);
        for (ClientProductResponse response : adminProductResponses) {
            for (Product product : resp) {
                response.setVariation(convertProductDetailsToVariants(product.getProductDetails()));
            }
        }
        return new PageableObject<>(adminProductResponses);
    }

    @Override
    public ClientProductResponse create(ClientProductRequest request) {
        return null;
    }

    @Override
    public ClientProductResponse update(ClientProductRequest request) {
        return null;
    }

    @Override
    public ClientProductResponse findById(UUID id) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }

    public Set<ClientVariantResponse> convertProductDetailsToVariants(Set<ProductDetail> productDetails) {
        Map<String, ClientVariantResponse> colorVariantMap = new HashMap<>();

        for (ProductDetail productDetail : productDetails) {
            String color = productDetail.getColor().getName();
            String size = productDetail.getSize().getName();
            int quantity = productDetail.getQuantity();
            String material = productDetail.getMaterial().getName();
            String sole = productDetail.getSole().getName();
            String style = productDetail.getStyle().getName();
            String tradeMark = productDetail.getTradeMark().getName();
            String brand = productDetail.getBrand().getName();

            if (quantity > 0) {
                ClientVariantResponse variant = colorVariantMap.getOrDefault(color, new ClientVariantResponse());
                variant.setColor(color);
                variant.setBrand(brand);
                variant.setMaterial(material);
                variant.setStyle(style);
                variant.setTradeMark(tradeMark);
                variant.setSole(sole);
                variant.setPrice(productDetail.getPrice());
                variant.setImage(productDetail.getImage());
                List<ClientSizeQuantityResponse> sizeQuantities = variant.getSizeQuantities();
                if (sizeQuantities == null) {
                    sizeQuantities = new ArrayList<>();
                }

                boolean sizeExists = false;
                for (ClientSizeQuantityResponse sizeQuantity : sizeQuantities) {
                    if (sizeQuantity.getSize().equals(size)) {
                        sizeQuantity.setQuantity(sizeQuantity.getQuantity() + quantity);
                        sizeExists = true;
                        break;
                    }
                }

                if (!sizeExists) {
                    ClientSizeQuantityResponse newSizeQuantity = new ClientSizeQuantityResponse();
                    newSizeQuantity.setSize(size);
                    newSizeQuantity.setQuantity(quantity);
                    sizeQuantities.add(newSizeQuantity);
                }

                variant.setSizeQuantities(sizeQuantities);
                colorVariantMap.put(color, variant);
            }
        }

        return new HashSet<>(colorVariantMap.values());
    }


}
