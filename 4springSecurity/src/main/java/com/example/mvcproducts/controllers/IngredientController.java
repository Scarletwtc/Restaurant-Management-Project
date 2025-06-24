package com.example.mvcproducts.controllers;

import com.example.mvcproducts.domain.Ingredient;
import com.example.mvcproducts.domain.OrderItem;
import com.example.mvcproducts.repositories.IngredientRepository;
import com.example.mvcproducts.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public IngredientController(IngredientRepository ingredientRepository,
                              OrderItemRepository orderItemRepository) {
        this.ingredientRepository = ingredientRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    public String getAllIngredients(Model model) {
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "ingredients/list";
    }

    @GetMapping("/{id}")
    public String getIngredientById(@PathVariable Long id, Model model) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isPresent()) {
            model.addAttribute("ingredient", ingredient.get());
            return "ingredients/details";
        }
        return "redirect:/ingredients";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('CHEF')")
    public String newIngredientForm(Model model) {
        model.addAttribute("ingredient", new Ingredient());
        return "ingredients/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('CHEF')")
    public String createIngredient(@ModelAttribute Ingredient ingredient) {
        ingredient.setLastUpdated(new Date());
        ingredientRepository.save(ingredient);
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('CHEF')")
    public String editIngredientForm(@PathVariable Long id, Model model) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isPresent()) {
            model.addAttribute("ingredient", ingredient.get());
            return "ingredients/form";
        }
        return "redirect:/ingredients";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('CHEF')")
    public String updateIngredient(@PathVariable Long id, @ModelAttribute Ingredient ingredient) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findById(id);
        if (existingIngredient.isPresent()) {
            Ingredient updatedIngredient = existingIngredient.get();
            updatedIngredient.setName(ingredient.getName());
            updatedIngredient.setQuantity(ingredient.getQuantity());
            updatedIngredient.setUnit(ingredient.getUnit());
            updatedIngredient.setPrice(ingredient.getPrice());
            updatedIngredient.setExpiryDate(ingredient.getExpiryDate());
            updatedIngredient.setStatus(ingredient.getStatus());
            updatedIngredient.setLastUpdated(new Date());
            updatedIngredient.setThreshold(ingredient.getThreshold());
            
            ingredientRepository.save(updatedIngredient);
        }
        return "redirect:/ingredients";
    }

    @PostMapping("/{id}/mark-waste")
    @PreAuthorize("hasRole('CHEF')")
    public String markAsWaste(@PathVariable Long id, @RequestParam Double wasteAmount) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isPresent()) {
            Ingredient wastedIngredient = ingredient.get();
            wastedIngredient.addWaste(wasteAmount);
            ingredientRepository.save(wastedIngredient);
        }
        return "redirect:/ingredients";
    }

    @PostMapping("/add-from-order")
    @PreAuthorize("hasRole('CHEF')")
    public String addFromOrder(@RequestParam Long orderItemId) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
        if (orderItem.isPresent()) {
            OrderItem item = orderItem.get();
            Ingredient ingredient = new Ingredient(
                item.getItemName(),
                item.getQuantity(),
                item.getUnit()
            );
            ingredient.setPrice(item.getPrice());
            ingredient.setLastUpdated(new Date());
            ingredient.setStatus("ACTIVE");
            ingredientRepository.save(ingredient);
        }
        return "redirect:/ingredients";
    }

    @GetMapping("/low-stock")
    public String getLowStockIngredients(Model model) {
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "ingredients/low-stock";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('CHEF')")
    public String deleteIngredient(@PathVariable("id") Long id) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(id);
        if (ingredientOpt.isPresent()) {
            Ingredient ingredient = ingredientOpt.get();
            
            // First, remove any associated order items
            List<OrderItem> orderItems = orderItemRepository.findByIngredientId(id);
            for (OrderItem orderItem : orderItems) {
                orderItem.setIngredient(null);
                orderItemRepository.save(orderItem);
            }
            
            // Now we can safely delete the ingredient
            ingredientRepository.delete(ingredient);
        }
        return "redirect:/ingredients";
    }
} 