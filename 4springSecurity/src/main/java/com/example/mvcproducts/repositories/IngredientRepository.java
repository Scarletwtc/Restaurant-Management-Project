package com.example.mvcproducts.repositories;

import com.example.mvcproducts.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);
    
    List<Ingredient> findTop5ByOrderByLastUpdatedDesc();
    
    @Query("SELECT i FROM Ingredient i WHERE i.quantity < i.threshold AND i.status = 'ACTIVE'")
    List<Ingredient> findByQuantityLessThanThreshold();

    List<Ingredient> findAllByRecipe_Id(Long recipeId);

} 