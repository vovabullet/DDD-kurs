package com.fastfoodnetwork.inventory.infrastructure;

import com.fastfoodnetwork.inventory.domain.Product;
import com.fastfoodnetwork.inventory.domain.ProductRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация репозитория продуктов в памяти.
 * Используется для хранения данных о продуктах во время выполнения приложения.
 */
public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public InMemoryProductRepository() {
        // Инициализация тестовыми данными
        save(new Product("1", "Булки для бургеров", 100, LocalDate.now().plusDays(10), 50, 150, 20, "Комнатная температура"));
        save(new Product("2", "Говяжьи котлеты", 50, LocalDate.now().plusDays(5), 25, 75, 10, "Замороженный"));
        save(new Product("3", "Салат-латук", 20, LocalDate.now().plusDays(3), 10, 30, 5, "Охлажденный"));
        save(new Product("4", "Помидоры", 30, LocalDate.now().plusDays(4), 15, 45, 7, "Охлажденный"));
    }

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void deleteById(String id) {
        products.remove(id);
    }
}
