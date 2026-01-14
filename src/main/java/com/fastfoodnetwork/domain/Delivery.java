package com.fastfoodnetwork.domain;

import java.util.Date;

/**
 * Агрегат Поставка (Delivery).
 */
public class Delivery {
    private String id;
    private String supplyOrderId;
    private Date dispatchDate;
    private Date arrivalDate;
    private DeliveryStatus status;

    public Delivery(String id, String supplyOrderId) {
        this.id = id;
        this.supplyOrderId = supplyOrderId;
        this.dispatchDate = new Date();
        this.status = DeliveryStatus.DISPATCHED;
    }

    public void markAsArrived() {
        if (this.status == DeliveryStatus.DISPATCHED) {
            this.arrivalDate = new Date();
            this.status = DeliveryStatus.ARRIVED;
        } else {
            throw new IllegalStateException("Поставка может быть отмечена как прибывшая только из статуса DISPATCHED");
        }
    }

    public void markAsAccepted() {
        if (this.status == DeliveryStatus.ARRIVED) {
            this.status = DeliveryStatus.ACCEPTED;
        } else {
            throw new IllegalStateException("Продукты могут быть приняты только после прибытия поставки");
        }
    }

    public void markAsRejected() {
        if (this.status == DeliveryStatus.ARRIVED) {
            this.status = DeliveryStatus.REJECTED;
        } else {
            throw new IllegalStateException("Продукты могут быть отклонены только после прибытия поставки");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getSupplyOrderId() {
        return supplyOrderId;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public enum DeliveryStatus {
        DISPATCHED, ARRIVED, ACCEPTED, REJECTED
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id='" + id + '\'' +
                ", supplyOrderId='" + supplyOrderId + '\'' +
                ", dispatchDate=" + dispatchDate +
                ", arrivalDate=" + arrivalDate +
                ", status=" + status +
                '}';
    }
}
