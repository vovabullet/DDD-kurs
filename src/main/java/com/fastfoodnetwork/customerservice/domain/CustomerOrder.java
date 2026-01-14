package com.fastfoodnetwork.customerservice.domain;

import java.util.Date;
import java.util.List;

/**
 * Агрегат Клиентский заказ (CustomerOrder).
 */
public class CustomerOrder {
    private String id;
    private List<String> dishIds;
    private double totalPrice;
    private OrderStatus status;
    private Date orderDate;

    public CustomerOrder(String id, List<String> dishIds, double totalPrice) {
        this.id = id;
        this.dishIds = dishIds;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.PLACED;
        this.orderDate = new Date();
    }

    public void markAsInProgress() {
        if(this.status == OrderStatus.PLACED) {
            this.status = OrderStatus.IN_PROGRESS;
        } else {
            throw new IllegalStateException("Заказ может быть передан в работу только из статуса PLACED");
        }
    }

    public void complete() {
        if (this.status == OrderStatus.IN_PROGRESS) {
            this.status = OrderStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Завершить можно только заказ, который находится в работе");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public List<String> getDishIds() {
        return dishIds;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public enum OrderStatus {
        PLACED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id='" + id + '\'' +
                ", dishIds=" + dishIds +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", orderDate=" + orderDate +
                '}';
    }
}
