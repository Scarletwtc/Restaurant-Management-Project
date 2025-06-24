package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.Recipe;
import com.example.mvcproducts.repositories.RecipeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeService {

    private final RecipeRepository repo;

    public RecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasRole('CHEF')")
    public Recipe create(Recipe r) {
        return repo.save(r);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<Recipe> listAll() {
        return repo.findAll();
    }

    @PreAuthorize("hasRole('CHEF')")
    public Recipe updateDescription(Long id, String desc) {
        Recipe r = repo.findById(id).orElseThrow();
        r.setDescription(desc);
        return repo.save(r);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public double calculateCost(Long id) {
        Recipe r = repo.findById(id).orElseThrow();
        return r.calculateCost();
    }
}
