// WasteReport.java
package com.example.mvcproducts.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class WasteReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private Double quantityWasted;
    private String reason;

    @OneToMany(mappedBy = "wasteReport", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();

    public String generateReport() {
        return String.format(
                "Waste Report [%s]: wasted=%.2f for reason='%s'",
                date, quantityWasted, reason
        );
    }

    public void updateWasteData(Double newQuantity, String newReason) {
        this.quantityWasted = newQuantity;
        this.reason = newReason;
    }


    public void addIngredient(Ingredient ing) {
        ing.setWasteReport(this);
        this.ingredients.add(ing);
    }

}
