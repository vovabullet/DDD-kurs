package com.fastfoodnetwork.domain;

import java.time.LocalDate;

public class ExpiredProductException extends RuntimeException {
    public ExpiredProductException(String productId, LocalDate expiryDate) {
        super("Продукт " + productId + " просрочен (срок годности: " + expiryDate + ")");
    }
}
