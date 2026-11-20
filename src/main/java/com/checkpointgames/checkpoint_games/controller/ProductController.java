package com.checkpointgames.checkpoint_games.controller;

import com.checkpointgames.checkpoint_games.model.AgeRating;
import com.checkpointgames.checkpoint_games.model.Platform;
import com.checkpointgames.checkpoint_games.model.Product;
import com.checkpointgames.checkpoint_games.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        addFormOptions(model);
        return "product-form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // A dropdown left on "-- Select --" still creates an empty Brand/Category object
        if (product.getBrand() == null || product.getBrand().getId() == null) {
            result.rejectValue("brand", "required", "Please select a brand");
        }
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            result.rejectValue("category", "required", "Please select a category");
        }

        if (result.hasErrors()) {
            addFormOptions(model);
            return "product-form";
        }

        Product saved = productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
        return "redirect:/products/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        model.addAttribute("product", product);
        return "product-detail";
    }

    private void addFormOptions(Model model) {
        model.addAttribute("platforms", Platform.values());
        model.addAttribute("ageRatings", AgeRating.values());
        model.addAttribute("brands", productService.findAllBrands());
        model.addAttribute("categories", productService.findAllCategories());
    }
}