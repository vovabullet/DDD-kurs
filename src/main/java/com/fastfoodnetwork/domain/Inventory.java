package com.fastfoodnetwork.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private final ProductRepository productRepository;

    public Inventory(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void useProduct(String productId, int quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            int newQuantity = product.getQuantity() - quantity;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Not enough product in stock");
            }
            product.setQuantity(newQuantity);
            productRepository.save(product);
        });
    }

    public void writeOffExpiredProducts() {
        productRepository.findAll().forEach(product -> {
            if (product.getExpiryDate().isBefore(LocalDate.now())) {
                productRepository.deleteById(product.getId());
            }
        });
    }

    public void adjustInventory(String productId, int actualQuantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setQuantity(actualQuantity);
            productRepository.save(product);
        });
    }

    public List<Product> getProductsWithCriticalStockLevel() {
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() <= product.getCriticalLevel())
                .collect(Collectors.toList());
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
