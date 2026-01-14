package com.fastfoodnetwork.purchasing.domain;

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

    public void markAsAccepted(boolean qualityCheckPassed) {
        if (this.status != DeliveryStatus.ARRIVED) {
            throw new IllegalStateException("Продукты могут быть приняты только после прибытия поставки");
        }
        if (qualityCheckPassed) {
            this.status = DeliveryStatus.ACCEPTED;
        } else {
            markAsRejected("Контроль качества не пройден");
        }
    }

    public void markAsRejected(String reason) {
        if (this.status != DeliveryStatus.ARRIVED) {
            throw new IllegalStateException("Продукты могут быть отклонены только после прибытия поставки");
        }
        this.status = DeliveryStatus.REJECTED;
        // Здесь можно было бы сохранить причину `reason` в поле, если бы оно было
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
