package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.ExpiryAlert;
import com.example.mvcproducts.domain.Ingredient;
import com.example.mvcproducts.repositories.ExpiryAlertRepository;
import com.example.mvcproducts.repositories.IngredientRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepo;
    private final ExpiryAlertRepository alertRepo;

    public IngredientService(IngredientRepository ingredientRepo,
                             ExpiryAlertRepository alertRepo) {
        this.ingredientRepo = ingredientRepo;
        this.alertRepo = alertRepo;
    }

    @PreAuthorize("hasRole('CHEF')")
    public Ingredient create(Ingredient i) {
        i.setLastUpdated(new Date());
        i.setStatus("ACTIVE");
        return ingredientRepo.save(i);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public Ingredient findById(Long id) {
        return ingredientRepo.findById(id).orElse(null);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<Ingredient> listAll() {
        return ingredientRepo.findAll();
    }

    @PreAuthorize("hasRole('CHEF')")
    public Ingredient updateQuantity(Long id, Double newQty) {
        Ingredient i = findById(id);
        i.updateQuantity(newQty);
        return ingredientRepo.save(i);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<Ingredient> findLowStock() {
        return ingredientRepo.findByQuantityLessThanThreshold();
    }

    @PreAuthorize("hasRole('CHEF')")
    public void checkAndTriggerExpiryAlerts() {
        Date today = new Date();
        ingredientRepo.findAll().stream()
                .filter(i -> i.checkExpiry(today))
                .forEach(i -> {
                    ExpiryAlert alert = new ExpiryAlert();
                    alert.setAlertDate(today);
                    alert.triggerAlert();
                    alert.getIngredients().add(i);
                    alertRepo.save(alert);
                });
    }
}
