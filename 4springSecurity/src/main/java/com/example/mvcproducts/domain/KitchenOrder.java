// KitchenOrder.java
package com.example.mvcproducts.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class KitchenOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderTime;
    private String status;
    private String orderDetails;

    @OneToMany(mappedBy = "kitchenOrder", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Recipe> recipes = new HashSet<>();

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public void addOrderDetails(String details) {
        this.orderDetails = (this.orderDetails == null ? "" : this.orderDetails) + details;
    }
}
