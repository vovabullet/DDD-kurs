package com.fastfoodnetwork.ui;

import com.fastfoodnetwork.application.InventoryService;
import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.infrastructure.InMemoryProductRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final InventoryService inventoryService = new InventoryService(new InMemoryProductRepository());
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMainMenu();
            int choice = getIntInput("");
            switch (choice) {
                case 1:
                    manageInventory();
                    break;
                case 2:
                    manageMenu();
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
        System.out.println("2. Управление меню");
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

    private void manageMenu() {
        System.out.println("Функционал управления меню находится в разработке.");
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
