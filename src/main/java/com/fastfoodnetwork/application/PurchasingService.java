package com.fastfoodnetwork.application;

import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.domain.Delivery;
import com.fastfoodnetwork.domain.DeliveryRepository;
import com.fastfoodnetwork.domain.SupplyOrder;
import com.fastfoodnetwork.domain.SupplyOrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Сервис для управления закупками.
 */
public class PurchasingService {
    private final SupplyOrderRepository supplyOrderRepository;
    private final DeliveryRepository deliveryRepository;
    private final InventoryService inventoryService;

    public PurchasingService(SupplyOrderRepository supplyOrderRepository, DeliveryRepository deliveryRepository, InventoryService inventoryService) {
        this.supplyOrderRepository = supplyOrderRepository;
        this.deliveryRepository = deliveryRepository;
        this.inventoryService = inventoryService;
    }

    /**
     * Создает новый заказ поставщику.
     */
    public SupplyOrder createSupplyOrder(String id, String supplierId, Map<String, Integer> products) {
        SupplyOrder order = new SupplyOrder(id, supplierId, products);
        supplyOrderRepository.save(order);
        return order;
    }

    /**
     * Подтверждает заказ.
     */
    public void confirmSupplyOrder(String orderId) {
        SupplyOrder order = findOrderOrThrow(orderId);
        order.confirm();
        supplyOrderRepository.save(order);
    }

    /**
     * Отправляет заказ поставщику и создает поставку.
     */
    public Delivery sendSupplyOrder(String orderId) {
        SupplyOrder order = findOrderOrThrow(orderId);
        order.send();
        supplyOrderRepository.save(order);

        String deliveryId = orderId + "-delivery";
        Delivery delivery = new Delivery(deliveryId, orderId);
        deliveryRepository.save(delivery);
        return delivery;
    }

    /**
     * Обрабатывает прибытие поставки.
     */
    public void markDeliveryAsArrived(String deliveryId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.markAsArrived();
        deliveryRepository.save(delivery);
    }

    /**
     * Принимает продукты из поставки и добавляет их в инвентарь.
     */
    public void acceptDelivery(String deliveryId) {
        Delivery delivery = findDeliveryOrThrow(deliveryId);
        delivery.markAsAccepted();
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

    public List<SupplyOrder> getAllSupplyOrders() {
        return supplyOrderRepository.findAll();
    }

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
