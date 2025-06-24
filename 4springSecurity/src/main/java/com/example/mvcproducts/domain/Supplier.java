// Supplier.java
package com.example.mvcproducts.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor @ToString(exclude="purchaseOrders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactInfo;

    @OneToMany(mappedBy = "supplier")
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    public Supplier(String name) {
        this.name = name;
    }

    public void updateContactInfo(String newContact) {
        this.contactInfo = newContact;
    }

    public String getPerformanceMetrics() {
        // TODO: flesh out real metrics calculation
        return "Performance metrics for supplier '" + name + "'";
    }
}