package com.fastfoodnetwork.menu.domain;

import java.util.Map;

/**
 * Сущность Блюдо (Dish).
 */
public class Dish {
    private String id;
    private String name;
    private String description;
    private double price;
    private Map<String, Integer> requiredProducts; // Map<productId, quantity>

    public Dish(String id, String name, String description, double price, Map<String, Integer> requiredProducts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.requiredProducts = requiredProducts;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Map<String, Integer> getRequiredProducts() {
        return requiredProducts;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", requiredProducts=" + requiredProducts +
                '}';
    }
}
