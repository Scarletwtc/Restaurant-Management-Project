package com.example.mvcproducts.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Denormalized copy of the name at time of order (optional)
    private String itemName;

    private Double quantity;
    private String unit;
    private Double price;
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    // single FK to the Ingredient you ordered
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public void calculateSubtotal() {
        this.subtotal = this.quantity * this.price;
    }

    public Double getSubtotal() {
        if (this.subtotal == null) calculateSubtotal();
        return this.subtotal;
    }
}
