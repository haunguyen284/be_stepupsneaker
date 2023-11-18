package com.ndt.be_stepupsneaker.core.client.dto.request.order;

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
public class ClientOrderDetailRequest extends PageableRequest {

    private UUID id;

    @NotBlank(message = "Product detail must be not null")
    private UUID productDetail;

    @NotBlank(message = "Order must be not null")
    private UUID order;

    @NotNull(message = "Quantity must be not null")
    private int quantity;

    @NotNull(message = "Price must be not null")
    private float price;

    @NotNull(message = "Total price must be not null")
    private float totalPrice;

    @NotNull(message = "Status must be not null")
    private OrderStatus status;

//    Filters

    private UUID tradeMark;

    private UUID style;

    private UUID size;

    private UUID product;

    private UUID material;

    private UUID color;

    private UUID brand;

    private UUID sole;

}
