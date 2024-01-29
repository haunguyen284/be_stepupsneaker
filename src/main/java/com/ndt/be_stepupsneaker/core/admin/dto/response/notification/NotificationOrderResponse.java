package com.ndt.be_stepupsneaker.core.admin.dto.response.notification;

import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationOrderResponse {
    private OrderStatus status;
    private int count;
}
