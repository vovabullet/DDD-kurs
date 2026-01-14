package com.fastfoodnetwork.inventory.domain;

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
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.minimumStock = minimumStock;
        this.optimalStock = optimalStock;
        this.criticalLevel = criticalLevel;
        this.temperatureMode = temperatureMode;
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
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public int getOptimalStock() {
        return optimalStock;
    }

    public void setOptimalStock(int optimalStock) {
        this.optimalStock = optimalStock;
    }

    public int getCriticalLevel() {
        return criticalLevel;
    }

    public void setCriticalLevel(int criticalLevel) {
        this.criticalLevel = criticalLevel;
    }

    public String getTemperatureMode() {
        return temperatureMode;
    }

    public void setTemperatureMode(String temperatureMode) {
        this.temperatureMode = temperatureMode;
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
}
