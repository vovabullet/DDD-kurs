package com.fastfoodnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Агрегат Меню (Menu).
 */
public class Menu {
    private String id;
    private String restaurantId;
    private List<Dish> dishes;

    public Menu(String id, String restaurantId) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishes = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        if (dishes.stream().anyMatch(d -> d.getId().equals(dish.getId()))) {
            throw new IllegalArgumentException("Блюдо с ID " + dish.getId() + " уже существует в меню.");
        }
        dishes.add(dish);
    }

    public void removeDish(String dishId) {
        if (dishes.stream().noneMatch(d -> d.getId().equals(dishId))) {
            throw new IllegalArgumentException("Блюдо с ID " + dishId + " не найдено в меню.");
        }
        dishes.removeIf(dish -> dish.getId().equals(dishId));
    }

    public Optional<Dish> findDishById(String dishId) {
        return dishes.stream().filter(d -> d.getId().equals(dishId)).findFirst();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public List<Dish> getDishes() {
        return new ArrayList<>(dishes);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}
