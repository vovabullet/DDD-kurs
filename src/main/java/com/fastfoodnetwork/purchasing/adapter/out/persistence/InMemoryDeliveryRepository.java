package com.fastfoodnetwork.purchasing.adapter.out.persistence;

import com.fastfoodnetwork.purchasing.application.port.out.DeliveryRepository;
import com.fastfoodnetwork.purchasing.domain.Delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryDeliveryRepository implements DeliveryRepository {
    private final Map<String, Delivery> deliveries = new HashMap<>();

    @Override
    public void save(Delivery delivery) {
        deliveries.put(delivery.getId(), delivery);
    }

    @Override
    public Optional<Delivery> findById(String id) {
        return Optional.ofNullable(deliveries.get(id));
    }

    @Override
    public List<Delivery> findAll() {
        return new ArrayList<>(deliveries.values());
    }
}
