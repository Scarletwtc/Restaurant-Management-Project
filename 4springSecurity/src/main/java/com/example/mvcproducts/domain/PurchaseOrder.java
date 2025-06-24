// PurchaseOrder.java
package com.example.mvcproducts.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor @ToString(exclude="orderItems")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderDate;
    private String status;
    private Double totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();

    public PurchaseOrder(String status, Supplier supplier) {
        this.status = status;
        this.supplier = supplier;
        this.orderDate = new Date();
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setPurchaseOrder(this);
    }

    public Double calculateTotalCost() {
        return orderItems.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}