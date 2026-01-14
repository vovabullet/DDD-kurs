package com.fastfoodnetwork.purchasing.adapter.out.notification;

import com.fastfoodnetwork.purchasing.application.port.out.SupplierNotificationPort;

public class ConsoleSupplierNotifier implements SupplierNotificationPort {
    @Override
    public void notifyOrderSent(String orderId, String supplierId) {
        System.out.println("--- УВЕДОМЛЕНИЕ ПОСТАВЩИКУ ---");
        System.out.println("Поставщику " + supplierId + ": Заказ " + orderId + " был отправлен.");
        System.out.println("-----------------------------");
    }

    @Override
    public void notifyReturnProcessed(String orderId, String reason) {
        System.out.println("--- УВЕДОМЛЕНИЕ ПОСТАВЩИКУ ---");
        System.out.println("По заказу " + orderId + " был оформлен возврат по причине: " + reason);
        System.out.println("-----------------------------");
    }
}
