package com.fastfoodnetwork.domain;

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
        productRepository.findById(product.getId()).ifPresentOrElse(existing -> {
            existing.setQuantity(existing.getQuantity() + product.getQuantity());
            // Используем самый ранний срок годности, чтобы инвентарь оставался безопасным при смешении партий.
            if (product.getExpiryDate().isBefore(existing.getExpiryDate())) {
                existing.setExpiryDate(product.getExpiryDate());
            }
            existing.updateStockThresholds(product.getMinimumStock(), product.getOptimalStock(), product.getCriticalLevel());
            existing.setTemperatureMode(product.getTemperatureMode());
            productRepository.save(existing);
        }, () -> productRepository.save(product));
    }

    /**
     * Списывает указанное количество продукта.
     * @param productId ID продукта.
     * @param quantity количество для списания.
     */
    public void useProduct(String productId, int quantity) {
        if (quantity <= 0) {
            throw new InventoryValidationException("Количество для списания должно быть больше нуля");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.isExpired(LocalDate.now())) {
            throw new ExpiredProductException(productId, product.getExpiryDate());
        }

        int newQuantity = product.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw new InsufficientStockException(productId, quantity, product.getQuantity());
        }

        product.setQuantity(newQuantity);
        productRepository.save(product);
    }

    /**
     * Списывает все просроченные продукты.
     */
    public void writeOffExpiredProducts() {
        LocalDate today = LocalDate.now();
        productRepository.findAll().forEach(product -> {
            if (product.isExpired(today)) {
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
        if (actualQuantity < 0) {
            throw new InventoryValidationException("Фактическое количество не может быть отрицательным");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.setQuantity(actualQuantity);
        productRepository.save(product);
    }

    /**
     * Возвращает список продуктов, у которых достигнут критический уровень запасов.
     * @return список продуктов.
     */
    public List<Product> getProductsWithCriticalStockLevel() {
        return productRepository.findAll().stream()
                .filter(Product::isCriticalLevelReached)
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
