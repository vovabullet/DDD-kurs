package com.fastfoodnetwork.purchasing.application.service;

import com.fastfoodnetwork.inventory.domain.Product;
import com.fastfoodnetwork.purchasing.application.port.in.SupplyOrderUseCase;
import com.fastfoodnetwork.purchasing.application.port.out.DeliveryRepository;
import com.fastfoodnetwork.purchasing.application.port.out.SupplierNotificationPort;
import com.fastfoodnetwork.purchasing.application.port.out.SupplyOrderRepository;
import com.fastfoodnetwork.purchasing.domain.Delivery;
import com.fastfoodnetwork.purchasing.domain.SupplyOrder;
import com.fastfoodnetwork.inventory.application.InventoryService;
import com.fastfoodnetwork.inventory.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupplyOrderService implements SupplyOrderUseCase {
    private final SupplyOrderRepository supplyOrderRepository;
    private final DeliveryRepository deliveryRepository;
    private final SupplierNotificationPort notificationPort;
    private final InventoryService inventoryService;

    public SupplyOrderService(SupplyOrderRepository supplyOrderRepository, DeliveryRepository deliveryRepository, SupplierNotificationPort notificationPort, InventoryService inventoryService) {
        this.supplyOrderRepository = supplyOrderRepository;
        this.deliveryRepository = deliveryRepository;
        this.notificationPort = notificationPort;
        this.inventoryService = inventoryService;
    }

    @Override
    public SupplyOrder createSupplyOrder(String id, String supplierId, Map<String, Integer> products) {
        SupplyOrder order = new SupplyOrder(id, supplierId, products);
        supplyOrderRepository.save(order);
        return order;
    }

    @Override
    public void confirmSupplyOrder(String orderId) {
        SupplyOrder order = findOrderOrThrow(orderId);
        order.confirm();
        supplyOrderRepository.save(order);
    }

    @Override
    public Delivery sendSupplyOrder(String orderId) {
        SupplyOrder order = findOrderOrThrow(orderId);
        order.send();
        supplyOrderRepository.save(order);

        String deliveryId = orderId + "-delivery";
        Delivery delivery = new Delivery(deliveryId, orderId);
        deliveryRepository.save(delivery);
        notificationPort.notifyOrderSent(order.getId(), order.getSupplierId());
        return delivery;
    }

    @Override
    public void markDeliveryAsArrived(String deliveryId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.markAsArrived();
        deliveryRepository.save(delivery);
    }

    @Override
    public void acceptDelivery(String deliveryId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.markAsAccepted(true); // true - контроль качества пройден
        deliveryRepository.save(delivery);

        SupplyOrder order = findOrderOrThrow(delivery.getSupplyOrderId());
        order.markAsDelivered();
        supplyOrderRepository.save(order);

        // Добавляем продукты в инвентарь
        for (Map.Entry<String, Integer> entry : order.getProducts().entrySet()) {
            String productId = entry.getKey();
            Integer quantity = entry.getValue();
            Optional<Product> productOpt = inventoryService.findProductById(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                inventoryService.adjustInventory(productId, product.getQuantity() + quantity);
            } else {
                 // В реальном приложении здесь была бы логика создания нового продукта
                 System.out.println("ПРЕДУПРЕЖДЕНИЕ: Продукт с ID " + productId + " не найден в инвентаре. Продукт не добавлен.");
            }
        }
    }

    @Override
    public void processReturn(String deliveryId, String reason) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.markAsRejected(reason);
        deliveryRepository.save(delivery);
        notificationPort.notifyReturnProcessed(delivery.getSupplyOrderId(), reason);
    }

    @Override
    public List<SupplyOrder> getAllSupplyOrders() {
        return supplyOrderRepository.findAll();
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    private SupplyOrder findOrderOrThrow(String orderId) {
        return supplyOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + " не найден"));
    }

    private Delivery findDeliveryOrThrow(String deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Поставка с ID " + deliveryId + " не найдена"));
    }
}
