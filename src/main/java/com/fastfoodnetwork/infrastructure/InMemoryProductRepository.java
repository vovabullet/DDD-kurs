package com.fastfoodnetwork.infrastructure;

import com.fastfoodnetwork.domain.Product;
import com.fastfoodnetwork.domain.ProductRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public InMemoryProductRepository() {
        // Initialize with some test data
        save(new Product("1", "Burger Buns", 100, LocalDate.now().plusDays(10), 50, 150, 20, "Room Temperature"));
        save(new Product("2", "Beef Patties", 50, LocalDate.now().plusDays(5), 25, 75, 10, "Frozen"));
        save(new Product("3", "Lettuce", 20, LocalDate.now().plusDays(3), 10, 30, 5, "Refrigerated"));
        save(new Product("4", "Tomatoes", 30, LocalDate.now().plusDays(4), 15, 45, 7, "Refrigerated"));
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
