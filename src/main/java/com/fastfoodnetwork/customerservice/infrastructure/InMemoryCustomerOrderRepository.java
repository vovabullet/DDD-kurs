package com.fastfoodnetwork.customerservice.infrastructure;

import com.fastfoodnetwork.customerservice.domain.CustomerOrder;
import com.fastfoodnetwork.customerservice.domain.CustomerOrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerOrderRepository implements CustomerOrderRepository {
    private final Map<String, CustomerOrder> orders = new HashMap<>();

    @Override
    public void save(CustomerOrder order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<CustomerOrder> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<CustomerOrder> findAll() {
        return new ArrayList<>(orders.values());
    }
}
