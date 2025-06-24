package com.example.mvcproducts.controllers;

import com.example.mvcproducts.domain.WasteReport;
import com.example.mvcproducts.domain.Ingredient;
import com.example.mvcproducts.repositories.WasteReportRepository;
import com.example.mvcproducts.repositories.IngredientRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/waste-reports")
public class WasteReportController {

    private final WasteReportRepository wasteReportRepo;
    private final IngredientRepository ingredientRepo;

    public WasteReportController(WasteReportRepository wasteReportRepo,
                                 IngredientRepository ingredientRepo) {
        this.wasteReportRepo = wasteReportRepo;
        this.ingredientRepo   = ingredientRepo;
    }

    // expose ingredients to both new & edit forms
    @ModelAttribute("ingredients")
    public List<Ingredient> allIngredients() {
        return ingredientRepo.findAll();
    }

    // ─── LIST ───────────────────────────────────────────────────────────────────────
    @GetMapping
    public String list(Model model) {
        model.addAttribute("wasteReports", wasteReportRepo.findAll());
        return "waste-reports/list";
    }

    // ─── DETAILS ────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        WasteReport wr = wasteReportRepo.findById(id).orElseThrow();
        model.addAttribute("wasteReport", wr);
        return "waste-reports/details";
    }

    // ─── FORM (new AND edit) ───────────────────────────────────────────────────────
    @GetMapping({"/new", "/{id}/edit"})
    @PreAuthorize("hasRole('CHEF')")
    public String form(@PathVariable(required = false) Long id, Model model) {
        WasteReport wr = (id == null)
                ? new WasteReport()
                : wasteReportRepo.findById(id).orElseThrow();
        model.addAttribute("wasteReport", wr);
        return "waste-reports/form";
    }

    @PostMapping({ "", "/{id}" })
    @PreAuthorize("hasRole('CHEF')")
    public String save(
            @PathVariable(required = false) Long id,
            @ModelAttribute WasteReport wasteReport,
            @RequestParam(value="ingredientIds", required=false) Long[] ingredientIds
    ) {
        boolean isNew = (id == null);

        if (isNew) {
            wasteReport.setDate(new Date());
            wasteReport = wasteReportRepo.save(wasteReport);

            double totalWasted = 0;
            if (ingredientIds != null) {
                for (Long ingId : ingredientIds) {
                    Ingredient ing = ingredientRepo.findById(ingId).orElseThrow();
                    ing.setWasteReport(wasteReport);
                    ing.markAsRotten();
                    ingredientRepo.save(ing);
                    totalWasted += ing.getQuantity() * ing.getPrice();
                }
            }
            wasteReport.setQuantityWasted(totalWasted);
        } else {
            WasteReport existing = wasteReportRepo.findById(id).orElseThrow();
            existing.setReason(wasteReport.getReason());
            wasteReport = existing;
        }

        wasteReportRepo.save(wasteReport);
        return "redirect:/waste-reports";
    }
}
