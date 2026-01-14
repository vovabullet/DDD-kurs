package com.fastfoodnetwork.purchasing.application.port.out;

import com.fastfoodnetwork.purchasing.domain.Delivery;
import java.util.Optional;
import java.util.List;

public interface DeliveryRepository {
    void save(Delivery delivery);
    Optional<Delivery> findById(String id);
    List<Delivery> findAll();
}
