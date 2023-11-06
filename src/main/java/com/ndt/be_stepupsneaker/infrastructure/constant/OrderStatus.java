package com.ndt.be_stepupsneaker.infrastructure.constant;

public enum OrderStatus {
    PENDING("Preparing order"),
    WAIT_FOR_CONFIRMATION("Order is waiting for confirmation"),
    WAIT_FOR_DELIVERY("Order is ready"),
    DELIVERING("Order is being delivered"),
    COMPLETED("Order completed"),
    CANCELED("Order is cancelled"),
    EXPIRED("Order is expired"),
    RETURNED("Order is returned"),
    EXCHANGED("Order is exchanged");

    public final String action_description;

    private OrderStatus(String action_description) {
        this.action_description = action_description;
    }
}
