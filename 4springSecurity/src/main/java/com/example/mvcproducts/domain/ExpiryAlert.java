// ExpiryAlert.java
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
public class ExpiryAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date alertDate;
    private String status;

    @OneToMany(mappedBy = "expiryAlert", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Ingredient> ingredients = new HashSet<>();

    public void triggerAlert() {
        this.status = "TRIGGERED";
    }

    public void resolveAlert() {
        this.status = "RESOLVED";
    }
}
