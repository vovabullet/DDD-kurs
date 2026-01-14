package com.fastfoodnetwork.ui;

import com.fastfoodnetwork.application.InventoryService;
import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.infrastructure.InMemoryProductRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final InventoryService inventoryService = new InventoryService(new InMemoryProductRepository());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
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
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- FastFood Network Inventory Management ---");
        System.out.println("1. Add Product");
        System.out.println("2. Use Product");
        System.out.println("3. Adjust Inventory");
        System.out.println("4. Write Off Expired Products");
        System.out.println("5. Generate Stock Report");
        System.out.println("6. Show Products with Critical Stock Level");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        int quantity = getIntInput("Enter quantity: ");
        LocalDate expiryDate = getLocalDateInput("Enter expiry date (yyyy-MM-dd): ");
        int minimumStock = getIntInput("Enter minimum stock: ");
        int optimalStock = getIntInput("Enter optimal stock: ");
        int criticalLevel = getIntInput("Enter critical level: ");
        System.out.print("Enter temperature mode: ");
        String temperatureMode = scanner.nextLine();

        inventoryService.addProduct(id, name, quantity, expiryDate, minimumStock, optimalStock, criticalLevel, temperatureMode);
        System.out.println("Product added successfully.");
    }

    private static void useProduct() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        int quantity = getIntInput("Enter quantity to use: ");

        try {
            inventoryService.useProduct(id, quantity);
            System.out.println("Product used successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void adjustInventory() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        int quantity = getIntInput("Enter actual quantity: ");

        inventoryService.adjustInventory(id, quantity);
        System.out.println("Inventory adjusted successfully.");
    }

    private static void writeOffExpiredProducts() {
        inventoryService.writeOffExpiredProducts();
        System.out.println("Expired products written off successfully.");
    }

    private static void generateStockReport() {
        System.out.println(inventoryService.generateStockReport());
    }

    private static void showCriticalStockProducts() {
        List<Product> criticalStockProducts = inventoryService.getProductsWithCriticalStockLevel();
        if (criticalStockProducts.isEmpty()) {
            System.out.println("No products with critical stock level.");
        } else {
            System.out.println("Products with critical stock level:");
            criticalStockProducts.forEach(System.out::println);
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static LocalDate getLocalDateInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }
}
