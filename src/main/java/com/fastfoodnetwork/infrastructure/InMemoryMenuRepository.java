package com.fastfoodnetwork.infrastructure;

import com.fastfoodnetwork.domain.Dish;
import com.fastfoodnetwork.domain.Menu;
import com.fastfoodnetwork.domain.MenuRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMenuRepository implements MenuRepository {
    private final Map<String, Menu> menus = new HashMap<>();

    public InMemoryMenuRepository() {
        // тестовое меню
        Menu mainMenu = new Menu("1", "1");
        mainMenu.addDish(new Dish("1", "Классический Бургер", "Бургер с говяжьей котлетой", 350.0, Map.of("1", 2, "2", 1, "3", 1)));
        mainMenu.addDish(new Dish("2", "Картофель Фри", "Хрустящий картофель", 150.0, Map.of("5", 1)));
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
