package com.fastfoodnetwork.menu.infrastructure;

import com.fastfoodnetwork.menu.domain.Dish;
import com.fastfoodnetwork.menu.domain.Menu;
import com.fastfoodnetwork.menu.domain.MenuRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMenuRepository implements MenuRepository {
    private final Map<String, Menu> menus = new HashMap<>();

    public InMemoryMenuRepository() {
        // Создаем тестовое меню для ресторана "main_restaurant"
        Menu mainMenu = new Menu("main_menu", "main_restaurant");
        mainMenu.addDish(new Dish("d1", "Классический Бургер", "Бургер с говяжьей котлетой", 350.0, Map.of("1", 2, "2", 1, "3", 1)));
        mainMenu.addDish(new Dish("d2", "Картофель Фри", "Хрустящий картофель", 150.0, Map.of("5", 1))); // Предполагаем, что картофель - продукт с ID 5
        menus.put(mainMenu.getId(), mainMenu);
    }

    @Override
    public void save(Menu menu) {
        menus.put(menu.getId(), menu);
    }

    @Override
    public Optional<Menu> findById(String id) {
        return Optional.ofNullable(menus.get(id));
    }

    @Override
    public Optional<Menu> findByRestaurantId(String restaurantId) {
        return menus.values().stream()
                .filter(menu -> menu.getRestaurantId().equals(restaurantId))
                .findFirst();
    }
}
