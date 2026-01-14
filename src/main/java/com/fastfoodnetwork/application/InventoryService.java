package com.fastfoodnetwork.application;

import com.fastfoodnetwork.domain.Inventory;
import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.domain.ProductRepository;

import java.time.LocalDate;
import java.util.List;

public class InventoryService {
    private final Inventory inventory;
    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.inventory = new Inventory(productRepository);
    }

    public void addProduct(String id, String name, int quantity, LocalDate expiryDate, int minimumStock, int optimalStock, int criticalLevel, String temperatureMode) {
        Product product = new Product(id, name, quantity, expiryDate, minimumStock, optimalStock, criticalLevel, temperatureMode);
        inventory.addProduct(product);
    }

    public void useProduct(String productId, int quantity) {
        inventory.useProduct(productId, quantity);
    }

    public void writeOffExpiredProducts() {
        inventory.writeOffExpiredProducts();
    }

    public void adjustInventory(String productId, int actualQuantity) {
        inventory.adjustInventory(productId, actualQuantity);
    }

    public String generateStockReport() {
        StringBuilder report = new StringBuilder("Current Stock Report:\n");
        inventory.getAllProducts().forEach(product -> report.append(product.toString()).append("\n"));
        return report.toString();
    }

    public List<Product> getProductsWithCriticalStockLevel() {
        return inventory.getProductsWithCriticalStockLevel();
    }

    public List<Product> getAllProducts() {
        return inventory.getAllProducts();
    }
}
