package com.example.mvcproducts.repositories;

import com.example.mvcproducts.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByPurchaseOrderId(Long purchaseOrderId);
    List<OrderItem> findByIngredientId(Long ingredientId);
} 