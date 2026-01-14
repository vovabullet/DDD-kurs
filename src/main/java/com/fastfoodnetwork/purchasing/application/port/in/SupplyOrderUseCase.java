package com.fastfoodnetwork.purchasing.application.port.in;

import com.fastfoodnetwork.purchasing.domain.Delivery;
import com.fastfoodnetwork.purchasing.domain.SupplyOrder;

import java.util.List;
import java.util.Map;

public interface SupplyOrderUseCase {
    SupplyOrder createSupplyOrder(String id, String supplierId, Map<String, Integer> products);
    void confirmSupplyOrder(String orderId);
    Delivery sendSupplyOrder(String orderId);
    void markDeliveryAsArrived(String deliveryId);
    void acceptDelivery(String deliveryId);
    void processReturn(String deliveryId, String reason);
    List<SupplyOrder> getAllSupplyOrders();
    List<Delivery> getAllDeliveries();
}
