package com.example.mvcproducts.bootstrap;

import com.example.mvcproducts.domain.*;
import com.example.mvcproducts.repositories.*;
import com.example.mvcproducts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.io.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;
    private final RecipeRepository recipeRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final WasteReportRepository wasteReportRepository;

    @Autowired
    public DataInitializer(UserService userService,
                           IngredientRepository ingredientRepository,
                           SupplierRepository supplierRepository,
                           RecipeRepository recipeRepository,
                           PurchaseOrderRepository purchaseOrderRepository,
                           OrderItemRepository orderItemRepository,
                           WasteReportRepository wasteReportRepository) {
        this.userService = userService;
        this.ingredientRepository = ingredientRepository;
        this.supplierRepository = supplierRepository;
        this.recipeRepository = recipeRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository     = orderItemRepository;
        this.wasteReportRepository     = wasteReportRepository;

    }

    @Override
    public void run(String... args) throws Exception {
        createUsers();
        createSuppliers();
        createIngredients();
        createRecipes();
        createPurchaseOrders();
        createWasteReports();
    }

    private void createUsers() {
        userService.registerChef("chef", "password");
        userService.registerSupplier("supplier", "password");
        userService.registerOwner("owner", "password");
        
        System.out.println("Created default users: chef, supplier, owner (all with password 'password')");
    }

    private void createSuppliers() {
        Supplier supplier1 = new Supplier("Farm Fresh Produce");
        supplier1.setContactInfo("123-456-7890, john@farmfresh.com");
        supplierRepository.save(supplier1);

        Supplier supplier2 = new Supplier("Meats & Poultry Inc.");
        supplier2.setContactInfo("987-654-3210, sales@meatspoultry.com");
        supplierRepository.save(supplier2);

        Supplier supplier3 = new Supplier("Global Spice Traders");
        supplier3.setContactInfo("555-123-4567, info@globalspice.com");
        supplierRepository.save(supplier3);

        Supplier s4 = new Supplier("Dairy Delights");
        s4.setContactInfo("444-222-1111, dairy@delights.com");
        supplierRepository.save(s4);

        Supplier s5 = new Supplier("Seafood Central");
        s5.setContactInfo("333-555-7777, seafood@central.com");
        supplierRepository.save(s5);

        System.out.println("Created 5 suppliers");
        
        System.out.println("Created suppliers");
    }

    private void createIngredients() {
        Ingredient i1 = new Ingredient("Tomatoes", 50.0, "kg");
        i1.setPrice(2.99); i1.setExpiryDate(new Date(System.currentTimeMillis() + 7L*24*60*60*1000)); i1.setThreshold(20.0);
        ingredientRepository.save(i1);

        Ingredient i2 = new Ingredient("Onions", 30.0, "kg");
        i2.setPrice(1.49); i2.setExpiryDate(new Date(System.currentTimeMillis() + 14L*24*60*60*1000)); i2.setThreshold(10.0);
        ingredientRepository.save(i2);

        Ingredient i3 = new Ingredient("Chicken", 25.0, "kg");
        i3.setPrice(5.99); i3.setExpiryDate(new Date(System.currentTimeMillis() + 4L*24*60*60*1000)); i3.setThreshold(10.0);
        ingredientRepository.save(i3);

        Ingredient i4 = new Ingredient("Beef", 20.0, "kg");
        i4.setPrice(8.99); i4.setExpiryDate(new Date(System.currentTimeMillis() + 5L*24*60*60*1000)); i4.setThreshold(8.0);
        ingredientRepository.save(i4);

        Ingredient i5 = new Ingredient("Salt", 5.0, "kg");
        i5.setPrice(0.99); i5.setExpiryDate(new Date(System.currentTimeMillis() + 365L*24*60*60*1000)); i5.setThreshold(1.0);
        ingredientRepository.save(i5);

        Ingredient i6 = new Ingredient("Black Pepper", 2.0, "kg");
        i6.setPrice(4.99); i6.setExpiryDate(new Date(System.currentTimeMillis() + 180L*24*60*60*1000)); i6.setThreshold(0.5);
        ingredientRepository.save(i6);

        Ingredient i7 = new Ingredient("Carrots", 40.0, "kg");
        i7.setPrice(1.29); i7.setExpiryDate(new Date(System.currentTimeMillis() + 10L*24*60*60*1000)); i7.setThreshold(15.0);
        ingredientRepository.save(i7);

        Ingredient i8 = new Ingredient("Potatoes", 60.0, "kg");
        i8.setPrice(0.99); i8.setExpiryDate(new Date(System.currentTimeMillis() + 30L*24*60*60*1000)); i8.setThreshold(20.0);
        ingredientRepository.save(i8);

        Ingredient i9 = new Ingredient("Garlic", 10.0, "kg");
        i9.setPrice(3.49); i9.setExpiryDate(new Date(System.currentTimeMillis() + 60L*24*60*60*1000)); i9.setThreshold(5.0);
        ingredientRepository.save(i9);

        Ingredient i10 = new Ingredient("Basil", 3.0, "kg");
        i10.setPrice(9.99); i10.setExpiryDate(new Date(System.currentTimeMillis() + 20L*24*60*60*1000)); i10.setThreshold(1.0);
        ingredientRepository.save(i10);

        Ingredient i11 = new Ingredient("Thyme",       2.0, "bunch");i11.setPrice(6.49);
        i11.setExpiryDate(new Date(System.currentTimeMillis() + 15L*24*3600*1000));i11.setThreshold(2.0); ingredientRepository.save(i11);
        Ingredient i12 = new Ingredient("Oregano",     2.0, "bunch");i12.setPrice(5.49);i12.setExpiryDate(new Date(System.currentTimeMillis()  + 15L*24*3600*1000));i12.setThreshold(2.0);
        ingredientRepository.save(i12);
        Ingredient i13 = new Ingredient("Spinach",    20.0, "kg"); i13.setPrice(2.49); i13.setExpiryDate(new Date(System.currentTimeMillis()  + 5L*24*3600*1000));  i13.setThreshold(5.0);
        ingredientRepository.save(i13);
        Ingredient i14 = new Ingredient("Mushrooms",  15.0, "kg"); i14.setPrice(7.99); i14.setExpiryDate(new Date(System.currentTimeMillis()  + 7L*24*3600*1000));  i14.setThreshold(3.0);
        ingredientRepository.save(i14);
        Ingredient i15 = new Ingredient("Milk",       30.0, "L");  i15.setPrice(1.09); i15.setExpiryDate(new Date(System.currentTimeMillis()  + 7L*24*3600*1000));  i15.setThreshold(10.0);
        ingredientRepository.save(i15);
        Ingredient i16 = new Ingredient("Cheddar Cheese",5.0,"kg");i16.setPrice(4.59);i16.setExpiryDate(new Date(System.currentTimeMillis()  + 30L*24*3600*1000));i16.setThreshold(2.0);
        ingredientRepository.save(i16);
        Ingredient i17 = new Ingredient("Flour",      40.0, "kg"); i17.setPrice(0.79); i17.setExpiryDate(new Date(System.currentTimeMillis()  + 180L*24*3600*1000));i17.setThreshold(15.0);
        ingredientRepository.save(i17);
        Ingredient i18 = new Ingredient("Sugar",      25.0, "kg"); i18.setPrice(1.19); i18.setExpiryDate(new Date(System.currentTimeMillis()  + 365L*24*3600*1000));i18.setThreshold(10.0);
        ingredientRepository.save(i18);
        Ingredient i19 = new Ingredient("Butter",     10.0, "kg"); i19.setPrice(2.19); i19.setExpiryDate(new Date(System.currentTimeMillis()  + 60L*24*3600*1000)); i19.setThreshold(3.0);
        ingredientRepository.save(i19);
        Ingredient i20 = new Ingredient("Eggs",       10.0, "dozen");i20.setPrice(0.29);i20.setExpiryDate(new Date(System.currentTimeMillis()  + 14L*24*3600*1000));i20.setThreshold(5.0);
        ingredientRepository.save(i20);



        System.out.println("Created ingredients");
    }

    private void createRecipes() {
        Recipe r1 = new Recipe(); r1.setName("Tomato Soup");          r1.setDescription("A classic tomato soup");  r1.setCost(10.5);         recipeRepository.save(r1);
        Recipe r2 = new Recipe(); r2.setName("Beef Stew");            r2.setDescription("Hearty beef stew");       r2.setCost(20.75);        recipeRepository.save(r2);
        Recipe r3 = new Recipe(); r3.setName("Chicken Curry");        r3.setDescription("Spicy chicken curry");     r3.setCost(17.20);       recipeRepository.save(r3);
        Recipe r4 = new Recipe(); r4.setName("Veggie Stir-Fry");      r4.setDescription("Colorful veggie stir-fry"); r4.setCost(15.30);      recipeRepository.save(r4);
        Recipe r5 = new Recipe(); r5.setName("Spaghetti Bolognese");  r5.setDescription("Rich meat sauce pasta");     r5.setCost(26.80);     recipeRepository.save(r5);
        Recipe r6 = new Recipe(); r6.setName("Garlic Mashed Potatoes");r6.setDescription("Fluffy garlic potatoes");  r6.setCost(13.25);     recipeRepository.save(r6);
        Recipe r7 = new Recipe(); r7.setName("Carrot Soup");          r7.setDescription("Sweet carrot soup");      r7.setCost(14.10);        recipeRepository.save(r7);
        Recipe r8 = new Recipe(); r8.setName("Pepper Steak");         r8.setDescription("Steak with black pepper");  r8.setCost(29.40);      recipeRepository.save(r8);
        Recipe r9 = new Recipe(); r9.setName("Herb Roasted Chicken"); r9.setDescription("Chicken with fresh herbs");   r9.setCost(18.00);    recipeRepository.save(r9);
        Recipe r10= new Recipe(); r10.setName("Potato Gratin");       r10.setDescription("Creamy potato bake");       r10.setCost(15.75);     recipeRepository.save(r10);
        Recipe r11 = new Recipe();  r11.setName("Mushroom Risotto");      r11.setDescription("Creamy risotto with wild mushrooms and Parmesan");r11.setCost(17.95);  recipeRepository.save(r11);
        Recipe r12 = new Recipe();  r12.setName("Beef Tacos");           r12.setDescription("Seasoned ground beef tacos with fresh salsa");  r12.setCost(36.50);  recipeRepository.save(r12);
        Recipe r13 = new Recipe();  r13.setName("Shrimp Pad Thai");       r13.setDescription("Thai noodles stir-fried with shrimp and tamarind"); r13.setCost(39.25); recipeRepository.save(r13);
        Recipe r14 = new Recipe();  r14.setName("Eggplant Parmesan");     r14.setDescription("Breaded eggplant baked with marinara and mozzarella"); r14.setCost(16.90); recipeRepository.save(r14);
        Recipe r15 = new Recipe();  r15.setName("Lentil Dahl");           r15.setDescription("Spiced red lentils simmered in coconut curry"); r15.setCost(15.15);    recipeRepository.save(r15);
        Recipe r16 = new Recipe();  r16.setName("BBQ Pulled ribs Sandwich"); r16.setDescription("Slow-cooked pork in tangy BBQ sauce on a bun"); r16.setCost(28.60); recipeRepository.save(r16);
        Recipe r17 = new Recipe();  r17.setName("Caesar Salad");         r17.setDescription("Crisp romaine with Parmesan, croutons, Caesar dressing");r17.setCost(9.95); recipeRepository.save(r17);
        Recipe r18 = new Recipe();  r18.setName("Pan-Seared Salmon");      r18.setDescription("Salmon fillet in lemon-butter sauce over spinach"); r18.setCost(20.20); recipeRepository.save(r18);
        Recipe r19 = new Recipe();  r19.setName("Stuffed Bell Peppers");  r19.setDescription("Peppers filled with seasoned rice, turkey, and cheese"); r19.setCost(11.35); recipeRepository.save(r19);
        Recipe r20 = new Recipe();  r20.setName("Chocolate Lava Cake");   r20.setDescription("Individual molten-center chocolate cakes"); r20.setCost(15.80);         recipeRepository.save(r20);


        System.out.println("Created recipes");
    }

    private void createPurchaseOrders() {
        Random rnd = new Random();
        var suppliers   = supplierRepository.findAll();
        var ingredients = ingredientRepository.findAll();

        for (int n = 1; n <= 5; n++) {
            PurchaseOrder po = new PurchaseOrder();
            po.setSupplier(suppliers.get(rnd.nextInt(suppliers.size())));
            po.setOrderDate(new Date());
            po.setStatus("PENDING");
            po = purchaseOrderRepository.save(po);

            double total = 0;
            int count = 2 + rnd.nextInt(3);
            for (int j = 0; j < count; j++) {
                Ingredient ing = ingredients.get(rnd.nextInt(ingredients.size()));
                OrderItem oi = new OrderItem();
                oi.setPurchaseOrder(po);
                oi.setIngredient(ing);
                oi.setItemName(ing.getName());
                double qty = 1 + rnd.nextInt(5);
                oi.setQuantity(qty);
                oi.setUnit(ing.getUnit());
                oi.setPrice(ing.getPrice());
                oi.calculateSubtotal();
                orderItemRepository.save(oi);
                total += oi.getSubtotal();
            }

            po.setTotalCost(total);
            purchaseOrderRepository.save(po);
        }

        System.out.println("Created 5 purchase orders with items");
    }
    private void createWasteReports() {
        Random rnd = new Random();
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        for (int i = 1; i <= 5; i++) {
            WasteReport wr = new WasteReport();
            wr.setDate(new Date());
            wr.setReason("Rotten batch #" + i);
            wr = wasteReportRepository.save(wr);

            double totalWasted = 0;
            // pick 2–4 random ingredients
            int picks = 2 + rnd.nextInt(3);
            for (int j = 0; j < picks; j++) {
                Ingredient ing = allIngredients.get(rnd.nextInt(allIngredients.size()));
                ing.markAsRotten();
                wr.addIngredient(ing);               // assumes you added a helper in WasteReport
                ing.setWasteReport(wr);
                ingredientRepository.save(ing);
                totalWasted += ing.getQuantity() * ing.getPrice();
            }

            wr.setQuantityWasted(totalWasted);
            wasteReportRepository.save(wr);
        }

        System.out.println("Created 5 waste reports with random rotten ingredients");
    }
} 