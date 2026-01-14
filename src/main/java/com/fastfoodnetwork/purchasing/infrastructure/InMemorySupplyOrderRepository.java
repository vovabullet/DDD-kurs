package com.fastfoodnetwork.purchasing.infrastructure;

import com.fastfoodnetwork.purchasing.domain.SupplyOrder;
import com.fastfoodnetwork.purchasing.domain.SupplyOrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySupplyOrderRepository implements SupplyOrderRepository {
    private final Map<String, SupplyOrder> orders = new HashMap<>();

    @Override
    public void save(SupplyOrder order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<SupplyOrder> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<SupplyOrder> findAll() {
        return new ArrayList<>(orders.values());
    }
}
