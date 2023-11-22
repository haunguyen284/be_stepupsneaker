package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientOrderDetailRequest extends PageableRequest {

    private String id;

    @NotBlank(message = "Product detail must be not null")
    private String productDetail;

    @NotBlank(message = "Order must be not null")
    private String order;

    @NotNull(message = "Quantity must be not null")
    private int quantity;

    @NotNull(message = "Price must be not null")
    private float price;

    //    @NotNull(message = "Total price must be not null")
    private float totalPrice;

    //    @NotNull(message = "Status must be not null")
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
