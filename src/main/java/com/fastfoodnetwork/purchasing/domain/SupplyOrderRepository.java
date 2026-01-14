package com.fastfoodnetwork.purchasing.domain;

import java.util.List;
import java.util.Optional;

public interface SupplyOrderRepository {
    void save(SupplyOrder order);
    Optional<SupplyOrder> findById(String id);
    List<SupplyOrder> findAll();
}
