package com.fastfoodnetwork.purchasing.application.port.out;

public interface SupplierNotificationPort {
    void notifyOrderSent(String orderId, String supplierId);
    void notifyReturnProcessed(String orderId, String reason);
}
