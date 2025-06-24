package com.example.mvcproducts.controllers;

import com.example.mvcproducts.domain.Ingredient;
import com.example.mvcproducts.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@PreAuthorize("hasRole('OWNER')")
public class DashboardController {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WasteReportRepository wasteReportRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public DashboardController(IngredientRepository ingredientRepository,
                              RecipeRepository recipeRepository,
                              PurchaseOrderRepository purchaseOrderRepository,
                              WasteReportRepository wasteReportRepository,
                              SupplierRepository supplierRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.wasteReportRepository = wasteReportRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public String getDashboard(Model model) {
        // Add counts of key entities
        model.addAttribute("ingredientCount", ingredientRepository.count());
        model.addAttribute("recipeCount", recipeRepository.count());
        model.addAttribute("purchaseOrderCount", purchaseOrderRepository.count());
        model.addAttribute("wasteReportCount", wasteReportRepository.count());
        model.addAttribute("supplierCount", supplierRepository.count());
        
        // Add recent entities
        model.addAttribute("recentIngredients", ingredientRepository.findTop5ByOrderByLastUpdatedDesc());
        model.addAttribute("recentPurchaseOrders", purchaseOrderRepository.findTop5ByOrderByOrderDateDesc());
        model.addAttribute("recentWasteReports", wasteReportRepository.findTop5ByOrderByDateDesc());
        
        // Add aggregated data for overview
        model.addAttribute("pendingOrdersCount", purchaseOrderRepository.countByStatus("PENDING"));
        model.addAttribute("approvedOrdersCount", purchaseOrderRepository.countByStatus("APPROVED"));
        model.addAttribute("rejectedOrdersCount", purchaseOrderRepository.countByStatus("REJECTED"));
        
        // Include any low stock ingredients that have fallen below their threshold
        model.addAttribute("lowStockIngredients", ingredientRepository.findByQuantityLessThanThreshold());
        
        return "dashboard/index";
    }

    @GetMapping("/inventory")
    public String getInventoryReport(Model model) {
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "dashboard/inventory";
    }

    @GetMapping("/orders")
    public String getOrdersReport(Model model) {
        model.addAttribute("purchaseOrders", purchaseOrderRepository.findAll());
        return "dashboard/orders";
    }

    @GetMapping("/waste")
    public String getWasteReport(Model model) {
        // Get all ingredients that have waste
        List<Ingredient> wastedIngredients = ingredientRepository.findAll().stream()
            .filter(ingredient -> ingredient.getWasteAmount() > 0)
            .collect(Collectors.toList());
        
        model.addAttribute("wastedIngredients", wastedIngredients);
        return "dashboard/waste";
    }

    @GetMapping("/suppliers")
    public String getSupplierReport(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "dashboard/suppliers";
    }
} 