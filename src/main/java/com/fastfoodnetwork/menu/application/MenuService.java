package com.fastfoodnetwork.menu.application;

import com.fastfoodnetwork.inventory.application.InventoryService;
import com.fastfoodnetwork.inventory.domain.Product;
import com.fastfoodnetwork.menu.domain.Dish;
import com.fastfoodnetwork.menu.domain.Menu;
import com.fastfoodnetwork.menu.domain.MenuRepository;

import java.util.Map;
import java.util.Optional;

/**
 * Сервис для управления меню.
 */
public class MenuService {
    private final MenuRepository menuRepository;
    private final InventoryService inventoryService;

    public MenuService(MenuRepository menuRepository, InventoryService inventoryService) {
        this.menuRepository = menuRepository;
        this.inventoryService = inventoryService;
    }

    public Menu getMenuForRestaurant(String restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Меню для ресторана " + restaurantId + " не найдено."));
    }

    public void addDishToMenu(String menuId, Dish dish) {
        Menu menu = findMenuOrThrow(menuId);
        menu.addDish(dish);
        menuRepository.save(menu);
    }

    public void removeDishFromMenu(String menuId, String dishId) {
        Menu menu = findMenuOrThrow(menuId);
        menu.removeDish(dishId);
        menuRepository.save(menu);
    }

    /**
     * Проверяет, можно ли приготовить блюдо с учетом текущих запасов.
     */
    public boolean isDishAvailable(String menuId, String dishId) {
        Menu menu = findMenuOrThrow(menuId);
        Dish dish = menu.findDishById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо с ID " + dishId + " не найдено в меню."));

        for (Map.Entry<String, Integer> requiredProduct : dish.getRequiredProducts().entrySet()) {
            String productId = requiredProduct.getKey();
            Integer requiredQuantity = requiredProduct.getValue();

            Optional<Product> productOpt = inventoryService.findProductById(productId);
            if (productOpt.isEmpty() || productOpt.get().getQuantity() < requiredQuantity) {
                return false; // Недостаточно продукта
            }
        }
        return true;
    }

    private Menu findMenuOrThrow(String menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Меню с ID " + menuId + " не найдено."));
    }
}
