package com.fastfoodnetwork.application;

import com.fastfoodnetwork.domain.Inventory;
import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.domain.ProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Сервис для управления инвентарем.
 * Предоставляет методы для взаимодействия с инвентарем.
 */
public class InventoryService {
    private final Inventory inventory;
    private final ProductRepository productRepository;
    private PurchasingService purchasingService; // Зависимость для политики

    /**
     * Конструктор для создания сервиса инвентаризации.
     * @param productRepository репозиторий для доступа к данным о продуктах.
     */
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.inventory = new Inventory(productRepository);
    }

    // Сеттер для внедрения зависимости, чтобы избежать цикличности
    public void setPurchasingService(PurchasingService purchasingService) {
        this.purchasingService = purchasingService;
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

        // Политика: Автоматический заказ при достижении критического уровня
        productRepository.findById(productId).ifPresent(product -> {
            if (product.getQuantity() <= product.getCriticalLevel()) {
                System.out.println("ПОЛИТИКА: Продукт '" + product.getName() + "' достиг критического уровня. Создание автоматического заказа...");
                if (purchasingService != null) {
                    String orderId = "auto-order-" + productId + "-" + System.currentTimeMillis();
                    // Заказываем количество, равное оптимальному запасу
                    purchasingService.createSupplyOrder(orderId, "default-supplier", Map.of(productId, product.getOptimalStock()));
                    System.out.println("ПОЛИТИКА: Автоматический заказ '" + orderId + "' успешно создан.");
                } else {
                    System.out.println("ПОЛИТИКА ПРЕДУПРЕЖДЕНИЕ: PurchasingService не установлен, автоматический заказ не создан.");
                }
            }
        });
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
        inventory.getAllProducts().forEach(product -> report.append(product.toString()).append("\n"));
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

    /**
     * Находит продукт по ID.
     */
    public Optional<Product> findProductById(String productId) {
        return productRepository.findById(productId);
    }
}
