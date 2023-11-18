package com.ndt.be_stepupsneaker.core.client.dto.response.product;

import com.ndt.be_stepupsneaker.entity.product.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientVariantResponse {

    private String color;
    private String brand;
    private String material;
    private String style;
    private String sole;
    private String tradeMark;
//    private List<ClientSizeResponse> sizes;
    private List<ClientSizeQuantityResponse> sizeQuantities;
    private Float price;
    private String image;

}
