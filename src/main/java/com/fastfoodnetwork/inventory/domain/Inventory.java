package com.fastfoodnetwork.inventory.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Этот класс представляет инвентарь ресторана.
 * Он управляет бизнес-логикой, связанной с продуктами.
 */
public class Inventory {
    private final ProductRepository productRepository;

    /**
     * Конструктор для создания инвентаря с репозиторием продуктов.
     * @param productRepository репозиторий для доступа к данным о продуктах.
     */
    public Inventory(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Добавляет продукт в инвентарь.
     * @param product продукт для добавления.
     */
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    /**
     * Списывает указанное количество продукта.
     * @param productId ID продукта.
     * @param quantity количество для списания.
     */
    public void useProduct(String productId, int quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            int newQuantity = product.getQuantity() - quantity;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Недостаточно продукта на складе");
            }
            product.setQuantity(newQuantity);
            productRepository.save(product);
        });
    }

    /**
     * Списывает все просроченные продукты.
     */
    public void writeOffExpiredProducts() {
        productRepository.findAll().forEach(product -> {
            if (product.getExpiryDate().isBefore(LocalDate.now())) {
                productRepository.deleteById(product.getId());
            }
        });
    }

    /**
     * Корректирует количество продукта до фактического значения.
     * @param productId ID продукта.
     * @param actualQuantity фактическое количество.
     */
    public void adjustInventory(String productId, int actualQuantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setQuantity(actualQuantity);
            productRepository.save(product);
        });
    }

    /**
     * Возвращает список продуктов, у которых достигнут критический уровень запасов.
     * @return список продуктов.
     */
    public List<Product> getProductsWithCriticalStockLevel() {
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() <= product.getCriticalLevel())
                .collect(Collectors.toList());
    }

    /**
     * Возвращает все продукты в инвентаре.
     * @return список всех продуктов.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
