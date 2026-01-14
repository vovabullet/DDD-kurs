package com.fastfoodnetwork.menu.domain;

import java.util.Optional;

public interface MenuRepository {
    void save(Menu menu);
    Optional<Menu> findById(String id);
    Optional<Menu> findByRestaurantId(String restaurantId);
}
