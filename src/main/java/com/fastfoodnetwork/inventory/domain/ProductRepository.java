package com.fastfoodnetwork.inventory.domain;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для репозитория продуктов.
 * Определяет операции для управления данными о продуктах.
 */
public interface ProductRepository {
    /**
     * Сохраняет продукт.
     * @param product продукт для сохранения.
     */
    void save(Product product);

    /**
     * Находит продукт по ID.
     * @param id ID продукта.
     * @return Optional, содержащий продукт, если он найден.
     */
    Optional<Product> findById(String id);

    /**
     * Возвращает все продукты.
     * @return список всех продуктов.
     */
    List<Product> findAll();

    /**
     * Удаляет продукт по ID.
     * @param id ID продукта для удаления.
     */
    void deleteById(String id);
}
