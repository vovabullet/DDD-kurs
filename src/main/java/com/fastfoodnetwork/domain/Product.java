package com.fastfoodnetwork.domain;

import java.time.LocalDate;

public class Product {
    private String id;
    private String name;
    private int quantity;
    private LocalDate expiryDate;
    private int minimumStock;
    private int optimalStock;
    private int criticalLevel;
    private String temperatureMode;

    public Product(String id, String name, int quantity, LocalDate expiryDate, int minimumStock, int optimalStock, int criticalLevel, String temperatureMode) {
        this.id = requireText(id, "ID");
        this.name = requireText(name, "Название");
        validateQuantity(quantity);
        validateThresholds(minimumStock, optimalStock, criticalLevel);
        this.quantity = quantity;
        this.expiryDate = requireExpiryDate(expiryDate, currentDate());
        this.minimumStock = minimumStock;
        this.optimalStock = optimalStock;
        this.criticalLevel = criticalLevel;
        this.temperatureMode = requireText(temperatureMode, "Температурный режим");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        LocalDate today = currentDate();
        if (expiryDate == null || expiryDate.isBefore(today)) {
            throw new InventoryValidationException("Срок годности не может быть в прошлом");
        }
        this.expiryDate = expiryDate;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        updateStockThresholds(minimumStock, this.optimalStock, this.criticalLevel);
    }

    public int getOptimalStock() {
        return optimalStock;
    }

    public void setOptimalStock(int optimalStock) {
        updateStockThresholds(this.minimumStock, optimalStock, this.criticalLevel);
    }

    public int getCriticalLevel() {
        return criticalLevel;
    }

    public void setCriticalLevel(int criticalLevel) {
        updateStockThresholds(this.minimumStock, this.optimalStock, criticalLevel);
    }

    public String getTemperatureMode() {
        return temperatureMode;
    }

    public void setTemperatureMode(String temperatureMode) {
        this.temperatureMode = requireText(temperatureMode, "Температурный режим");
    }

    @Override
    public String toString() {
        return "Продукт: " +
                "ID='" + id + '\'' +
                ", Название='" + name + '\'' +
                ", Количество=" + quantity +
                ", Срок годности=" + expiryDate +
                ", Мин. запас=" + minimumStock +
                ", Оптимальный запас=" + optimalStock +
                ", Критический уровень=" + criticalLevel +
                ", Температурный режим='" + temperatureMode + '\'';
    }

    public boolean isExpired(LocalDate onDate) {
        return expiryDate.isBefore(onDate);
    }

    public boolean isCriticalLevelReached() {
        return quantity <= criticalLevel;
    }

    public void updateStockThresholds(int minimumStock, int optimalStock, int criticalLevel) {
        validateThresholds(minimumStock, optimalStock, criticalLevel);
        this.minimumStock = minimumStock;
        this.optimalStock = optimalStock;
        this.criticalLevel = criticalLevel;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InventoryValidationException(field + " обязателен для заполнения");
        }
        return value;
    }

    private static LocalDate requireExpiryDate(LocalDate expiryDate, LocalDate today) {
        if (expiryDate == null) {
            throw new InventoryValidationException("Срок годности обязателен для заполнения");
        }
        if (expiryDate.isBefore(today)) {
            throw new InventoryValidationException("Нельзя добавить просроченный продукт");
        }
        return expiryDate;
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InventoryValidationException("Количество не может быть отрицательным");
        }
    }

    private static void validateThresholds(int minimumStock, int optimalStock, int criticalLevel) {
        if (minimumStock < 0 || optimalStock < 0 || criticalLevel < 0) {
            throw new InventoryValidationException("Уровни запасов не могут быть отрицательными");
        }
        if (minimumStock > optimalStock) {
            throw new InventoryValidationException("Минимальный запас не может превышать оптимальный");
        }
        if (criticalLevel > minimumStock) {
            throw new InventoryValidationException("Критический уровень не может превышать минимальный запас");
        }
    }

    private static LocalDate currentDate() {
        return LocalDate.now();
    }
}
