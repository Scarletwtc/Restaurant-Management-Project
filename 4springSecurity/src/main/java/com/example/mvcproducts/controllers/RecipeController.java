package com.example.mvcproducts.controllers;

import com.example.mvcproducts.domain.Recipe;
import com.example.mvcproducts.domain.Ingredient;
import com.example.mvcproducts.repositories.RecipeRepository;
import com.example.mvcproducts.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public String getAllRecipes(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipes/list";
    }

    @GetMapping("/{id}")
    public String getRecipeById(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        List<Ingredient> ingredients = ingredientRepository.findAllByRecipe_Id(id);

        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredientsList", ingredients);
        return "recipes/details";
    }


    @GetMapping("/new")
    @PreAuthorize("hasRole('CHEF')")
    public String newRecipeForm(Model model) {
        Recipe recipe = new Recipe();
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredients", ingredientRepository.findAll());
        // no selected IDs yet
        model.addAttribute("selectedIngredientIds", List.of());
        return "recipes/form";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('CHEF')")
    public String editRecipeForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        List<Ingredient> all = ingredientRepository.findAll();
        List<Ingredient> attached = ingredientRepository.findAllByRecipe_Id(id);

        // collect IDs of the attached ingredients
        List<Long> selectedIds = attached.stream()
                .map(Ingredient::getId)
                .toList();

        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredients", all);
        model.addAttribute("selectedIngredientIds", selectedIds);
        return "recipes/form";
    }



    @PostMapping
    @PreAuthorize("hasRole('CHEF')")
    public String createRecipe(@ModelAttribute Recipe recipe, @RequestParam("ingredientIds") Long[] ingredientIds) {
        // Save the recipe first
        Recipe savedRecipe = recipeRepository.save(recipe);
        
        // Associate selected ingredients
        if (ingredientIds != null) {
            for (Long ingredientId : ingredientIds) {
                Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
                if (ingredient.isPresent()) {
                    Ingredient ing = ingredient.get();
                    ing.setRecipe(savedRecipe);
                    ingredientRepository.save(ing);
                }
            }
        }
        
        // Update the cost calculation based on ingredients
        savedRecipe.setCost(savedRecipe.calculateCost());
        recipeRepository.save(savedRecipe);
        
        return "redirect:/recipes";
    }


    @PostMapping("/{id}")
    @PreAuthorize("hasRole('CHEF')")
    public String updateRecipe(@PathVariable Long id,
                               @ModelAttribute Recipe recipe,
                               @RequestParam(value = "ingredientIds", required = false) Long[] ingredientIds) {
        Recipe updatedRecipe = recipeRepository.findById(id).orElseThrow();

        // 1) detach existing ingredients via a plain List
        List<Ingredient> currentlyAttached =
                ingredientRepository.findAllByRecipe_Id(id);
        for (Ingredient ing : currentlyAttached) {
            ing.setRecipe(null);
            ingredientRepository.save(ing);
        }

        // 2) update basic fields
        updatedRecipe.setName(recipe.getName());
        updatedRecipe.setDescription(recipe.getDescription());

        // 3) associate new ones
        if (ingredientIds != null) {
            for (Long ingredientId : ingredientIds) {
                ingredientRepository.findById(ingredientId).ifPresent(i -> {
                    i.setRecipe(updatedRecipe);
                    ingredientRepository.save(i);
                });
            }
        }

        // 4) recalc cost & save
        updatedRecipe.setCost(updatedRecipe.calculateCost());
        recipeRepository.save(updatedRecipe);

        return "redirect:/recipes";
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('CHEF')")
    public String deleteRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        // detach via a plain List
        List<Ingredient> attached = ingredientRepository.findAllByRecipe_Id(id);
        attached.forEach(i -> {
            i.setRecipe(null);
            ingredientRepository.save(i);
        });

        recipeRepository.delete(recipe);
        return "redirect:/recipes";
    }



} 