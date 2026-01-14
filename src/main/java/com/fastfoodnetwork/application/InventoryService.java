package com.fastfoodnetwork.application;

import com.fastfoodnetwork.domain.Inventory;
import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.domain.ProductRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для управления инвентарем.
 * Предоставляет методы для взаимодействия с инвентарем.
 */
public class InventoryService {
    private final Inventory inventory;
    private final ProductRepository productRepository;

    /**
     * Конструктор для создания сервиса инвентаризации.
     * @param productRepository репозиторий для доступа к данным о продуктах.
     */
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.inventory = new Inventory(productRepository);
    }

    /**
     * Добавляет новый продукт в инвентарь.
     */
    public void addProduct(String id, String name, int quantity, LocalDate expiryDate, int minimumStock, int optimalStock, int criticalLevel, String temperatureMode) {
        Product product = new Product(id, name, quantity, expiryDate, minimumStock, optimalStock, criticalLevel, temperatureMode);
        inventory.addProduct(product);
    }

    /**
     * Списывает продукт.
     */
    public void useProduct(String productId, int quantity) {
        inventory.useProduct(productId, quantity);
    }

    /**
     * Списывает просроченные продукты.
     */
    public void writeOffExpiredProducts() {
        inventory.writeOffExpiredProducts();
    }

    /**
     * Корректирует инвентарь.
     */
    public void adjustInventory(String productId, int actualQuantity) {
        inventory.adjustInventory(productId, actualQuantity);
    }

    /**
     * Генерирует отчет о запасах.
     */
    public String generateStockReport() {
        StringBuilder report = new StringBuilder("Отчет о текущих запасах:\n");
        LocalDate today = LocalDate.now();
        inventory.getAllProducts().forEach(product -> {
            report.append(product.toString());
            if (product.isExpired(today)) {
                report.append(" [ПРОСРОЧЕН]");
            }
            if (product.isCriticalLevelReached()) {
                report.append(" [КРИТИЧЕСКИЙ УРОВЕНЬ]");
            }
            report.append("\n");
        });
        return report.toString();
    }

    /**
     * Возвращает продукты с критическим уровнем запасов.
     */
    public List<Product> getProductsWithCriticalStockLevel() {
        return inventory.getProductsWithCriticalStockLevel();
    }

    /**
     * Возвращает все продукты.
     */
    public List<Product> getAllProducts() {
        return inventory.getAllProducts();
    }
}
