package com.fastfoodnetwork.ui;

import com.fastfoodnetwork.inventory.application.InventoryService;
import com.fastfoodnetwork.inventory.domain.Product;
import com.fastfoodnetwork.inventory.infrastructure.InMemoryProductRepository;
import com.fastfoodnetwork.purchasing.adapter.in.console.PurchasingConsoleAdapter;
import com.fastfoodnetwork.purchasing.application.port.in.SupplyOrderUseCase;
import com.fastfoodnetwork.purchasing.application.port.out.SupplierNotificationPort;
import com.fastfoodnetwork.purchasing.application.service.SupplyOrderService;
import com.fastfoodnetwork.purchasing.adapter.out.notification.ConsoleSupplierNotifier;
import com.fastfoodnetwork.purchasing.adapter.out.persistence.InMemoryDeliveryRepository;
import com.fastfoodnetwork.purchasing.adapter.out.persistence.InMemorySupplyOrderRepository;
import com.fastfoodnetwork.menu.application.MenuService;
import com.fastfoodnetwork.menu.domain.Dish;
import com.fastfoodnetwork.menu.domain.Menu;
import com.fastfoodnetwork.menu.infrastructure.InMemoryMenuRepository;
import com.fastfoodnetwork.customerservice.application.CustomerService;
import com.fastfoodnetwork.customerservice.domain.CustomerOrder;
import com.fastfoodnetwork.customerservice.infrastructure.InMemoryCustomerOrderRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private final InventoryService inventoryService;
    private final PurchasingConsoleAdapter purchasingConsoleAdapter;
    private final MenuService menuService;
    private final CustomerService customerService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI() {
        // Инициализация репозиториев и адаптеров вывода
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemorySupplyOrderRepository supplyOrderRepository = new InMemorySupplyOrderRepository();
        InMemoryDeliveryRepository deliveryRepository = new InMemoryDeliveryRepository();
        InMemoryMenuRepository menuRepository = new InMemoryMenuRepository();
        InMemoryCustomerOrderRepository customerOrderRepository = new InMemoryCustomerOrderRepository();
        SupplierNotificationPort supplierNotificationPort = new ConsoleSupplierNotifier();

        // Инициализация сервисов (ядро)
        this.inventoryService = new InventoryService(productRepository);
        SupplyOrderUseCase supplyOrderUseCase = new SupplyOrderService(supplyOrderRepository, deliveryRepository, supplierNotificationPort, inventoryService);
        this.menuService = new MenuService(menuRepository, inventoryService);
        this.customerService = new CustomerService(customerOrderRepository, menuService, inventoryService);

        // Внедрение зависимостей для политик (избегая циклов в конструкторе)
        inventoryService.setPurchasingService(supplyOrderUseCase);

        // Инициализация адаптеров ввода
        this.purchasingConsoleAdapter = new PurchasingConsoleAdapter(supplyOrderUseCase, scanner);
    }

    public void start() {
        while (true) {
            printMainMenu();
            int choice = getIntInput("");
            switch (choice) {
                case 1:
                    manageInventory();
                    break;
                case 2:
                    managePurchasing();
                    break;
                case 3:
                    manageMenu();
                    break;
                case 4:
                    manageCustomerService();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- FastFood Network ---");
        System.out.println("1. Управление инвентарем");
        System.out.println("2. Управление закупками");
        System.out.println("3. Управление меню");
        System.out.println("4. Клиентский сервис");
        System.out.println("0. Выход");
        System.out.print("Введите ваш выбор: ");
    }

    private void manageInventory() {
        while (true) {
            printInventoryMenu();
            int choice = getIntInput("");
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    useProduct();
                    break;
                case 3:
                    adjustInventory();
                    break;
                case 4:
                    writeOffExpiredProducts();
                    break;
                case 5:
                    generateStockReport();
                    break;
                case 6:
                    showCriticalStockProducts();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printInventoryMenu() {
        System.out.println("\n--- Управление инвентарем ---");
        System.out.println("1. Добавить продукт");
        System.out.println("2. Списать продукт");
        System.out.println("3. Скорректировать инвентарь");
        System.out.println("4. Списать просроченные продукты");
        System.out.println("5. Сгенерировать отчет о запасах");
        System.out.println("6. Показать продукты с критическим уровнем запасов");
        System.out.println("0. Назад");
        System.out.print("Введите ваш выбор: ");
    }

    private void managePurchasing() {
        purchasingConsoleAdapter.managePurchasing();
    }

    private void manageMenu() {
        while (true) {
            printMenuManagementMenu();
            int choice = getIntInput("");
            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    addDishToMenu();
                    break;
                case 3:
                    removeDishFromMenu();
                    break;
                case 4:
                    checkDishAvailability();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printMenuManagementMenu() {
        System.out.println("\n--- Управление меню ---");
        System.out.println("1. Посмотреть меню");
        System.out.println("2. Добавить блюдо в меню");
        System.out.println("3. Удалить блюдо из меню");
        System.out.println("4. Проверить доступность блюда");
        System.out.println("0. Назад");
        System.out.print("Введите ваш выбор: ");
    }

    private void viewMenu() {
        try {
            Menu menu = menuService.getMenuForRestaurant("main_restaurant");
            System.out.println("\n--- Меню для ресторана: " + menu.getRestaurantId() + " ---");
            if (menu.getDishes().isEmpty()) {
                System.out.println("Меню пустое.");
            } else {
                menu.getDishes().forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void manageCustomerService() {
        while (true) {
            printCustomerServiceMenu();
            int choice = getIntInput("");
            switch (choice) {
                case 1:
                    placeCustomerOrder();
                    break;
                case 2:
                    viewAllCustomerOrders();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printCustomerServiceMenu() {
        System.out.println("\n--- Клиентский сервис ---");
        System.out.println("1. Создать заказ");
        System.out.println("2. Посмотреть все заказы");
        System.out.println("0. Назад");
        System.out.print("Введите ваш выбор: ");
    }

    private void placeCustomerOrder() {
        try {
            System.out.print("Введите ID заказа: ");
            String orderId = scanner.nextLine();
            System.out.print("Введите ID ресторана: ");
            String restaurantId = scanner.nextLine();
            List<String> dishIds = new ArrayList<>();
            while (true) {
                System.out.print("Введите ID блюда (или 'готово' для завершения): ");
                String dishId = scanner.nextLine();
                if (dishId.equalsIgnoreCase("готово")) {
                    break;
                }
                dishIds.add(dishId);
            }
            if (dishIds.isEmpty()) {
                System.out.println("Заказ не может быть пустым.");
                return;
            }
            CustomerOrder order = customerService.placeOrder(orderId, dishIds, restaurantId);
            System.out.println("Заказ успешно создан. Общая стоимость: " + order.getTotalPrice());
            System.out.println("Со склада списаны необходимые ингредиенты.");
        } catch (Exception e) {
            System.out.println("Ошибка создания заказа: " + e.getMessage());
        }
    }

    private void viewAllCustomerOrders() {
        System.out.println("\n--- Все клиентские заказы ---");
        List<CustomerOrder> orders = customerService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    private void addDishToMenu() {
        try {
            System.out.print("Введите ID меню: ");
            String menuId = scanner.nextLine();
            System.out.print("Введите ID блюда: ");
            String dishId = scanner.nextLine();
            System.out.print("Введите название блюда: ");
            String name = scanner.nextLine();
            System.out.print("Введите описание блюда: ");
            String description = scanner.nextLine();
            System.out.print("Введите цену: ");
            double price = Double.parseDouble(scanner.nextLine());
            Map<String, Integer> requiredProducts = new HashMap<>();
            while (true) {
                System.out.print("Введите ID необходимого продукта (или 'готово' для завершения): ");
                String productId = scanner.nextLine();
                if (productId.equalsIgnoreCase("готово")) {
                    break;
                }
                int quantity = getIntInput("Введите необходимое количество: ");
                requiredProducts.put(productId, quantity);
            }
            Dish newDish = new Dish(dishId, name, description, price, requiredProducts);
            menuService.addDishToMenu(menuId, newDish);
            System.out.println("Блюдо успешно добавлено в меню.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void removeDishFromMenu() {
        try {
            System.out.print("Введите ID меню: ");
            String menuId = scanner.nextLine();
            System.out.print("Введите ID блюда для удаления: ");
            String dishId = scanner.nextLine();
            menuService.removeDishFromMenu(menuId, dishId);
            System.out.println("Блюдо успешно удалено из меню.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void checkDishAvailability() {
        try {
            System.out.print("Введите ID меню: ");
            String menuId = scanner.nextLine();
            System.out.print("Введите ID блюда для проверки: ");
            String dishId = scanner.nextLine();
            boolean isAvailable = menuService.isDishAvailable(menuId, dishId);
            if (isAvailable) {
                System.out.println("Блюдо '" + dishId + "' доступно для приготовления.");
            } else {
                System.out.println("Блюдо '" + dishId + "' недоступно из-за нехватки ингредиентов.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }


    private void addProduct() {
        System.out.print("Введите ID продукта: ");
        String id = scanner.nextLine();
        System.out.print("Введите название продукта: ");
        String name = scanner.nextLine();
        int quantity = getIntInput("Введите количество: ");
        LocalDate expiryDate = getLocalDateInput("Введите срок годности (гггг-ММ-дд): ");
        int minimumStock = getIntInput("Введите минимальный запас: ");
        int optimalStock = getIntInput("Введите оптимальный запас: ");
        int criticalLevel = getIntInput("Введите критический уровень: ");
        System.out.print("Введите температурный режим: ");
        String temperatureMode = scanner.nextLine();

        inventoryService.addProduct(id, name, quantity, expiryDate, minimumStock, optimalStock, criticalLevel, temperatureMode);
        System.out.println("Продукт успешно добавлен.");
    }

    private void useProduct() {
        System.out.print("Введите ID продукта: ");
        String id = scanner.nextLine();
        int quantity = getIntInput("Введите количество для списания: ");

        try {
            inventoryService.useProduct(id, quantity);
            System.out.println("Продукт успешно списан.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void adjustInventory() {
        System.out.print("Введите ID продукта: ");
        String id = scanner.nextLine();
        int quantity = getIntInput("Введите фактическое количество: ");

        inventoryService.adjustInventory(id, quantity);
        System.out.println("Инвентарь успешно скорректирован.");
    }

    private void writeOffExpiredProducts() {
        inventoryService.writeOffExpiredProducts();
        System.out.println("Просроченные продукты успешно списаны.");
    }

    private void generateStockReport() {
        System.out.println(inventoryService.generateStockReport());
    }

    private void showCriticalStockProducts() {
        List<Product> criticalStockProducts = inventoryService.getProductsWithCriticalStockLevel();
        if (criticalStockProducts.isEmpty()) {
            System.out.println("Нет продуктов с критическим уровнем запасов.");
        } else {
            System.out.println("Продукты с критическим уровнем запасов:");
            criticalStockProducts.forEach(System.out::println);
        }
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Неверный ввод. Пожалуйста, введите число: ");
            }
        }
    }

    private LocalDate getLocalDateInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.print("Неверный формат даты. Пожалуйста, используйте гггг-ММ-дд: ");
            }
        }
    }
}
