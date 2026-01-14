package com.fastfoodnetwork.purchasing.adapter.in.console;

import com.fastfoodnetwork.purchasing.application.port.in.SupplyOrderUseCase;
import com.fastfoodnetwork.purchasing.domain.Delivery;
import com.fastfoodnetwork.purchasing.domain.SupplyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PurchasingConsoleAdapter {
    private final SupplyOrderUseCase supplyOrderUseCase;
    private final Scanner scanner;

    public PurchasingConsoleAdapter(SupplyOrderUseCase supplyOrderUseCase, Scanner scanner) {
        this.supplyOrderUseCase = supplyOrderUseCase;
        this.scanner = scanner;
    }

    public void managePurchasing() {
        while (true) {
            printPurchasingMenu();
            int choice = getIntInput("");
            switch (choice) {
                case 1: createSupplyOrder(); break;
                case 2: confirmSupplyOrder(); break;
                case 3: sendSupplyOrder(); break;
                case 4: markDeliveryArrived(); break;
                case 5: acceptDelivery(); break;
                case 6: processReturn(); break;
                case 7: viewAllOrders(); break;
                case 8: viewAllDeliveries(); break;
                case 0: return;
                default: System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void printPurchasingMenu() {
        System.out.println("\n--- Управление закупками (Адаптер) ---");
        System.out.println("1. Создать заказ поставщику");
        System.out.println("2. Подтвердить заказ");
        System.out.println("3. Отправить заказ");
        System.out.println("4. Отметить прибытие поставки");
        System.out.println("5. Принять поставку");
        System.out.println("6. Оформить возврат");
        System.out.println("7. Посмотреть все заказы");
        System.out.println("8. Посмотреть все поставки");
        System.out.println("0. Назад");
        System.out.print("Введите ваш выбор: ");
    }

    private void createSupplyOrder() {
        System.out.print("Введите ID заказа: ");
        String orderId = scanner.nextLine();
        System.out.print("Введите ID поставщика: ");
        String supplierId = scanner.nextLine();
        Map<String, Integer> products = new HashMap<>();
        while (true) {
            System.out.print("Введите ID продукта (или 'готово' для завершения): ");
            String productId = scanner.nextLine();
            if (productId.equalsIgnoreCase("готово")) break;
            int quantity = getIntInput("Введите количество: ");
            products.put(productId, quantity);
        }
        supplyOrderUseCase.createSupplyOrder(orderId, supplierId, products);
        System.out.println("Заказ успешно создан через адаптер.");
    }

    private void confirmSupplyOrder() {
        System.out.print("Введите ID заказа для подтверждения: ");
        String orderId = scanner.nextLine();
        try {
            supplyOrderUseCase.confirmSupplyOrder(orderId);
            System.out.println("Заказ успешно подтвержден.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void sendSupplyOrder() {
        System.out.print("Введите ID заказа для отправки: ");
        String orderId = scanner.nextLine();
        try {
            Delivery delivery = supplyOrderUseCase.sendSupplyOrder(orderId);
            System.out.println("Заказ успешно отправлен. Создана поставка с ID: " + delivery.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void markDeliveryArrived() {
        System.out.print("Введите ID поставки для отметки о прибытии: ");
        String deliveryId = scanner.nextLine();
        try {
            supplyOrderUseCase.markDeliveryAsArrived(deliveryId);
            System.out.println("Поставка успешно отмечена как прибывшая.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void acceptDelivery() {
        System.out.print("Введите ID поставки для приемки: ");
        String deliveryId = scanner.nextLine();
        try {
            supplyOrderUseCase.acceptDelivery(deliveryId);
            System.out.println("Поставка успешно принята, продукты добавлены в инвентарь.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void processReturn() {
        System.out.print("Введите ID поставки для возврата: ");
        String deliveryId = scanner.nextLine();
        System.out.print("Введите причину возврата: ");
        String reason = scanner.nextLine();
        try {
            supplyOrderUseCase.processReturn(deliveryId, reason);
            System.out.println("Возврат по поставке " + deliveryId + " успешно оформлен.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void viewAllOrders() {
        System.out.println("\n--- Все заказы поставщикам ---");
        List<SupplyOrder> orders = supplyOrderUseCase.getAllSupplyOrders();
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    private void viewAllDeliveries() {
        System.out.println("\n--- Все поставки ---");
        List<Delivery> deliveries = supplyOrderUseCase.getAllDeliveries();
        if (deliveries.isEmpty()) {
            System.out.println("Поставок пока нет.");
        } else {
            deliveries.forEach(System.out::println);
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
}
