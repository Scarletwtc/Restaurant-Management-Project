package com.example.mvcproducts.repositories;

import com.example.mvcproducts.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findTop5ByOrderByOrderDateDesc();
    
    Long countByStatus(String status);
    List<PurchaseOrder> findByStatus(String status);


} 