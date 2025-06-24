package com.example.mvcproducts.repositories;

import com.example.mvcproducts.domain.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
    List<KitchenOrder> findByStatus(String status);
    List<KitchenOrder> findByOrderTimeBetween(Date start, Date end);
}
