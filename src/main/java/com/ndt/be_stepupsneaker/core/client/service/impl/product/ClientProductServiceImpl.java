package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductMapper;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientProductMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientProductService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Product;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientProductServiceImpl implements ClientProductService {
    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private ClientProductRepository clientProductRepository;

    @Override
    public PageableObject<ClientProductResponse> findAllEntity(ClientProductRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        List<ClientProductResponse> clientProductResponses = new ArrayList<>();

        Page<Object[]> resp = clientProductRepository.findAllProduct(request, pageable);
        for (Object[] result : resp) {
            Product product = (Product) result[0];
            ClientProductResponse clientProductResponse = ClientProductMapper.INSTANCE.productToClientProductResponse(product);
            clientProductResponse.setSaleCount((Long) result[1]);
            clientProductResponses.add(clientProductResponse);
        }
        Page<ClientProductResponse> clientProductResponsePage = new PageImpl<>(clientProductResponses, pageable, resp.getTotalElements());
        return new PageableObject<>(clientProductResponsePage);
    }

}
