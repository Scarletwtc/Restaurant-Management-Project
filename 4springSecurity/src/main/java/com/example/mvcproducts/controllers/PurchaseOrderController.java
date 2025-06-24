package com.example.mvcproducts.controllers;

import com.example.mvcproducts.domain.Ingredient;
import com.example.mvcproducts.domain.OrderItem;
import com.example.mvcproducts.domain.PurchaseOrder;
import com.example.mvcproducts.domain.Supplier;
import com.example.mvcproducts.repositories.IngredientRepository;
import com.example.mvcproducts.repositories.OrderItemRepository;
import com.example.mvcproducts.repositories.PurchaseOrderRepository;
import com.example.mvcproducts.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderRepository purchaseOrderRepo;
    private final OrderItemRepository      orderItemRepo;
    private final SupplierRepository       supplierRepo;
    private final IngredientRepository     ingredientRepo;

    @Autowired
    public PurchaseOrderController(PurchaseOrderRepository purchaseOrderRepo,
                                   OrderItemRepository orderItemRepo,
                                   SupplierRepository supplierRepo,
                                   IngredientRepository ingredientRepo) {
        this.purchaseOrderRepo = purchaseOrderRepo;
        this.orderItemRepo     = orderItemRepo;
        this.supplierRepo      = supplierRepo;
        this.ingredientRepo    = ingredientRepo;
    }

    // ─── LIST ───────────────────────────────────────────────────────────────────────
    @GetMapping("/chef")
    @PreAuthorize("hasRole('CHEF')")
    public String chefList(
            @RequestParam(value = "status", required = false) String status,
            Model model
    ) {
        List<PurchaseOrder> orders;
        if (status != null && !status.isBlank()) {
            orders = purchaseOrderRepo.findByStatus(status.toUpperCase());
        } else {
            orders = purchaseOrderRepo.findAll();
        }
        model.addAttribute("purchaseOrders", orders);
        model.addAttribute("filterStatus", status);
        return "purchase-orders-chef/list";
    }

    @GetMapping("/supplier")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String supplierList(
            @RequestParam(value = "status", required = false) String status,
            Model model
    ) {
        List<PurchaseOrder> orders;
        if (status != null && !status.isBlank()) {
            orders = purchaseOrderRepo.findByStatus(status.toUpperCase());
        } else {
            orders = purchaseOrderRepo.findAll();
        }
        model.addAttribute("purchaseOrders", orders);
        model.addAttribute("filterStatus", status);
        return "purchase-orders-supplier/list";
    }

    // Keep the original list method as a fallback
    @GetMapping
    public String list(
            @RequestParam(value = "status", required = false) String status,
            Model model,
            Authentication authentication
    ) {
        List<PurchaseOrder> orders;
        if (status != null && !status.isBlank()) {
            orders = purchaseOrderRepo.findByStatus(status.toUpperCase());
        } else {
            orders = purchaseOrderRepo.findAll();
        }
        model.addAttribute("purchaseOrders", orders);
        model.addAttribute("filterStatus", status);

        // Return different templates based on user role
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CHEF"))) {
            return "purchase-orders-chef/list";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPLIER"))) {
            return "purchase-orders-supplier/list";
        }
        
        // Fallback to original template for other roles
        return "purchase-orders/list";
    }


    // ─── DETAILS ────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        PurchaseOrder po = purchaseOrderRepo.findById(id).orElseThrow();
        // materialize items into a plain List for Thymeleaf
        List<OrderItem> items = new ArrayList<>(po.getOrderItems());
        model.addAttribute("purchaseOrder", po);
        model.addAttribute("items", items);
        return "purchase-orders/details";
    }

    // ─── NEW ────────────────────────────────────────────────────────────────────────
    @GetMapping("/new")
    @PreAuthorize("hasRole('CHEF')")
    public String newForm(Model model) {
        model.addAttribute("purchaseOrder", new PurchaseOrder());
        model.addAttribute("suppliers",     supplierRepo.findAll());
        model.addAttribute("ingredients",   ingredientRepo.findAll());
        return "purchase-orders/form";
    }

    // ─── CREATE ─────────────────────────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('CHEF')")
    public String create(
            @ModelAttribute PurchaseOrder purchaseOrder,
            @RequestParam("supplier.id")   Long   supplierId,
            @RequestParam("ingredientIds") Long[] ingredientIds,
            @RequestParam("quantities")    Double[] quantities
    ) {
        Supplier sup = supplierRepo.findById(supplierId).orElseThrow();
        purchaseOrder.setSupplier(sup);
        purchaseOrder.setStatus("PENDING");
        purchaseOrder.setOrderDate(new Date());

        PurchaseOrder saved = purchaseOrderRepo.save(purchaseOrder);

        for (int i = 0; i < ingredientIds.length; i++) {
            // Skip ingredients with quantity 0
            if (quantities[i] <= 0) {
                continue;
            }
            
            Ingredient ing = ingredientRepo.findById(ingredientIds[i]).orElseThrow();
            OrderItem item = new OrderItem();
            item.setPurchaseOrder(saved);
            item.setIngredient(ing);
            item.setItemName(ing.getName());
            item.setQuantity(quantities[i]);
            item.setUnit(ing.getUnit());
            item.setPrice(ing.getPrice());
            item.calculateSubtotal();
            orderItemRepo.save(item);
        }

        saved.setTotalCost(saved.calculateTotalCost());
        purchaseOrderRepo.save(saved);

        return "redirect:/purchase-orders";
    }

    // ─── EDIT ───────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('CHEF')")
    public String editForm(@PathVariable Long id, Model model) {
        PurchaseOrder po = purchaseOrderRepo.findById(id).orElseThrow();
        model.addAttribute("purchaseOrder", po);
        model.addAttribute("suppliers",     supplierRepo.findAll());
        model.addAttribute("ingredients",   ingredientRepo.findAll());

        // build arrays for the multi-row form
        List<OrderItem> items = new ArrayList<>(po.getOrderItems());
        Long[]   ingrIds = items.stream()
                .map(it -> it.getIngredient().getId())
                .toArray(Long[]::new);
        Double[] qtys    = items.stream()
                .map(OrderItem::getQuantity)
                .toArray(Double[]::new);

        model.addAttribute("ingredientIds", ingrIds);
        model.addAttribute("quantities",    qtys);

        return "purchase-orders/form";
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────────────
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('CHEF')")
    public String update(
            @PathVariable Long id,
            @ModelAttribute PurchaseOrder purchaseOrder,
            @RequestParam("supplier.id")   Long   supplierId,
            @RequestParam("ingredientIds") Long[] ingredientIds,
            @RequestParam("quantities")    Double[] quantities
    ) {
        PurchaseOrder existing = purchaseOrderRepo.findById(id).orElseThrow();
        existing.setSupplier(supplierRepo.findById(supplierId).orElseThrow());
        existing.setOrderDate(purchaseOrder.getOrderDate());

        // delete old items
        existing.getOrderItems().forEach(orderItemRepo::delete);
        existing.getOrderItems().clear();

        // add new items
        for (int i = 0; i < ingredientIds.length; i++) {
            // Skip ingredients with quantity 0
            if (quantities[i] <= 0) {
                continue;
            }
            
            Ingredient ing = ingredientRepo.findById(ingredientIds[i]).orElseThrow();
            OrderItem oi = new OrderItem();
            oi.setPurchaseOrder(existing);
            oi.setIngredient(ing);
            oi.setItemName(ing.getName());
            oi.setQuantity(quantities[i]);
            oi.setUnit(ing.getUnit());
            oi.setPrice(ing.getPrice());
            oi.calculateSubtotal();
            orderItemRepo.save(oi);
        }

        existing.setTotalCost(existing.calculateTotalCost());
        purchaseOrderRepo.save(existing);

        return "redirect:/purchase-orders";
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────────────
    // (You can keep this for programmatic use, but simply remove the "Delete" button from your list.html)
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('CHEF')")
    public String delete(@PathVariable Long id) {
        PurchaseOrder po = purchaseOrderRepo.findById(id).orElseThrow();
        po.getOrderItems().forEach(orderItemRepo::delete);
        purchaseOrderRepo.delete(po);
        return "redirect:/purchase-orders";
    }

    // ─── APPROVE / REJECT ─────────────────────────────────────────────────────────────
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String approve(@PathVariable Long id) {
        PurchaseOrder po = purchaseOrderRepo.findById(id).orElseThrow();
        po.setStatus("APPROVED");
        purchaseOrderRepo.save(po);

        // Update ingredients from approved order
        for (OrderItem item : po.getOrderItems()) {
            Ingredient existingIngredient = ingredientRepo.findByName(item.getItemName());
            if (existingIngredient != null) {
                // Update existing ingredient
                existingIngredient.setQuantity(existingIngredient.getQuantity() + item.getQuantity());
                existingIngredient.setLastUpdated(new Date());
                existingIngredient.setOrderItem(item);  // Link to the order item
                ingredientRepo.save(existingIngredient);
            } else {
                // Create new ingredient if it doesn't exist
                Ingredient newIngredient = new Ingredient(
                    item.getItemName(),
                    item.getQuantity(),
                    item.getUnit()
                );
                newIngredient.setPrice(item.getPrice());
                newIngredient.setLastUpdated(new Date());
                newIngredient.setStatus("ACTIVE");
                newIngredient.setOrderItem(item);
                ingredientRepo.save(newIngredient);
            }
        }

        return "redirect:/purchase-orders";
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('SUPPLIER')")
    public String reject(@PathVariable Long id) {
        PurchaseOrder po = purchaseOrderRepo.findById(id).orElseThrow();
        po.setStatus("REJECTED");
        purchaseOrderRepo.save(po);
        return "redirect:/purchase-orders";
    }

}
