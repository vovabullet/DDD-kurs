package com.fastfoodnetwork.purchasing.domain;

import java.util.Optional;
import java.util.List;

public interface DeliveryRepository {
    void save(Delivery delivery);
    Optional<Delivery> findById(String id);
    List<Delivery> findAll();
}
