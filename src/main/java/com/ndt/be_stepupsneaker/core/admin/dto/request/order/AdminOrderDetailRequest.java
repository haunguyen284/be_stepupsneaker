package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminOrderDetailRequest extends PageableRequest {

    private String id;

    private String productDetail;

    private String order;

    private int quantity;

    private float price;

    private float totalPrice;

    private OrderStatus status;

//    Filters

    private String tradeMark;

    private String style;

    private String size;

    private String product;

    private String material;

    private String color;

    private String brand;

    private String sole;

}
