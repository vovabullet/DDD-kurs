package com.fastfoodnetwork.domain;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Продукт с ID " + productId + " не найден в инвентаре");
    }
}
