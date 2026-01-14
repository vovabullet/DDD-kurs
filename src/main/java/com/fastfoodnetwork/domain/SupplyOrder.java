package com.fastfoodnetwork.domain;

import java.util.Date;
import java.util.Map;

/**
 * Агрегат Заказ поставщику (SupplyOrder).
 */
public class SupplyOrder {
    private String id;
    private String supplierId;
    private Map<String, Integer> products; // Map<productId, quantity>
    private Date orderDate;
    private OrderStatus status;

    public SupplyOrder(String id, String supplierId, Map<String, Integer> products) {
        this.id = id;
        this.supplierId = supplierId;
        this.products = products;
        this.orderDate = new Date();
        this.status = OrderStatus.CREATED;
    }

    public void confirm() {
        if (this.status == OrderStatus.CREATED) {
            this.status = OrderStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Заказ может быть подтвержден только из статуса CREATED");
        }
    }

    public void send() {
        if (this.status == OrderStatus.CONFIRMED) {
            this.status = OrderStatus.SENT;
        } else {
            throw new IllegalStateException("Заказ может быть отправлен только из статуса CONFIRMED");
        }
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public enum OrderStatus {
        CREATED, CONFIRMED, SENT, DELIVERED, CANCELLED
    }

    @Override
    public String toString() {
        return "SupplyOrder{" +
                "id='" + id + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", products=" + products +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
}
