package com.fastfoodnetwork.customerservice.domain;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository {
    void save(CustomerOrder order);
    Optional<CustomerOrder> findById(String id);
    List<CustomerOrder> findAll();
}
