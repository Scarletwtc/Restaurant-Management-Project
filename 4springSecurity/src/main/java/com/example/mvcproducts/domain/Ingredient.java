// src/main/java/com/example/data/domain/Ingredient.java
package com.example.mvcproducts.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Data
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double quantity;
    private String unit;
    private Double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private String status;
    private Date lastUpdated;
    private Double threshold;

    private Double wasteAmount = 0.0;  // Track amount of wasted items
    private Date lastWasteDate;        // Track when waste was last recorded

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expiry_alert_id")
    private ExpiryAlert expiryAlert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_report_id")
    private WasteReport wasteReport;

    public Ingredient(String name, Double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.lastUpdated = new Date();
        this.status = "ACTIVE";
    }

    public Double calculateSubtotal() {
        return this.quantity * this.price;
    }

    public void updateQuantity(Double newQuantity) {
        this.quantity = newQuantity;
        this.lastUpdated = new Date();
    }

    public boolean checkExpiry(Date currentDate) {
        return currentDate.after(this.expiryDate);
    }

    public void markAsRotten() {
        this.status = "ROTTEN";
    }

    public void addWaste(Double amount) {
        if (amount > 0 && amount <= this.quantity) {
            this.wasteAmount += amount;
            this.quantity -= amount;
            this.lastWasteDate = new Date();
            this.lastUpdated = new Date();
        }
    }
}