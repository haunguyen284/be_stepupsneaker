package com.ndt.be_stepupsneaker.infrastructure.constant;

public enum ReturnDeliveryStatus {
    PENDING("Đang chuẩn bị"),
    RETURNING("Đang được giao đi"),
    RECEIVED("Đã nhận được"),
    COMPLETED("Đã hoàn thành");

    public final String action_description;

    private ReturnDeliveryStatus(String action_description) {
        this.action_description = action_description;
    }
}
