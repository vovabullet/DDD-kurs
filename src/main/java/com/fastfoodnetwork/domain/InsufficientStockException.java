package com.fastfoodnetwork.domain;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId, int requested, int available) {
        super("Недостаточно продукта " + productId + ": запрошено " + requested + ", доступно " + available);
    }
}
