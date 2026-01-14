package com.fastfoodnetwork.application;

import com.fastfoodnetwork.domain.CustomerOrder;
import com.fastfoodnetwork.domain.CustomerOrderRepository;
import com.fastfoodnetwork.domain.Dish;
import com.fastfoodnetwork.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * Сервис для управления клиентскими заказами.
 */
public class CustomerService {
    private final CustomerOrderRepository customerOrderRepository;
    private final MenuService menuService;
    private final InventoryService inventoryService;

    public CustomerService(CustomerOrderRepository customerOrderRepository, MenuService menuService, InventoryService inventoryService) {
        this.customerOrderRepository = customerOrderRepository;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
    }

    /**
     * Создает новый клиентский заказ.
     * Проверяет доступность всех блюд и списывает ингредиенты.
     */
    public CustomerOrder placeOrder(String id, List<String> dishIds, String restaurantId) {
        Menu menu = menuService.getMenuForRestaurant(restaurantId);

        // 1. Проверить, что все блюда существуют в меню и доступны
        for (String dishId : dishIds) {
            if (menu.findDishById(dishId).isEmpty()) {
                throw new IllegalArgumentException("Блюдо с ID " + dishId + " не найдено в меню.");
            }
            if (!menuService.isDishAvailable(menu.getId(), dishId)) {
                throw new IllegalStateException("Блюдо с ID " + dishId + " недоступно для заказа из-за нехватки ингредиентов.");
            }
        }

        // 2. Рассчитать общую стоимость
        double totalPrice = dishIds.stream()
                                   .map(dishId -> menu.findDishById(dishId).get().getPrice())
                                   .mapToDouble(Double::doubleValue)
                                   .sum();

        // 3. Списать ингредиенты
        for (String dishId : dishIds) {
            Dish dish = menu.findDishById(dishId).get();
            for (Map.Entry<String, Integer> entry : dish.getRequiredProducts().entrySet()) {
                inventoryService.useProduct(entry.getKey(), entry.getValue());
            }
        }

        // 4. Создать и сохранить заказ
        CustomerOrder order = new CustomerOrder(id, dishIds, totalPrice);
        customerOrderRepository.save(order);
        return order;
    }

    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }
}
