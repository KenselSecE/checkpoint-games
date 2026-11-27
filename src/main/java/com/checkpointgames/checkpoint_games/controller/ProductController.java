package com.checkpointgames.checkpoint_games.controller;

import com.checkpointgames.checkpoint_games.model.AgeRating;
import com.checkpointgames.checkpoint_games.model.Platform;
import com.checkpointgames.checkpoint_games.model.Product;
import com.checkpointgames.checkpoint_games.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(@RequestParam(required = false) Platform platform,
                               @RequestParam(required = false) Long brandId,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(defaultValue = "createdAt") String sortBy,
                               @RequestParam(defaultValue = "desc") String direction,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {
        Page<Product> products = productService.search(
                platform, brandId, categoryId, sortBy, direction, page, 6);

        model.addAttribute("products", products);
        model.addAttribute("platform", platform);
        model.addAttribute("brandId", brandId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("platforms", Platform.values());
        model.addAttribute("brands", productService.findAllBrands());
        model.addAttribute("categories", productService.findAllCategories());
        return "products";
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

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        model.addAttribute("product", product);
        addFormOptions(model);
        return "product-form";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        product.setId(id);   // keeps the form in "edit mode" if there are errors
        checkSelections(product, result);

        if (result.hasErrors()) {
            addFormOptions(model);
            return "product-form";
        }

        productService.update(id, product);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/products/" + id;
    }

    // A dropdown left on "-- Select --" still creates an empty Brand/Category object
    private void checkSelections(Product product, BindingResult result) {
        if (product.getBrand() == null || product.getBrand().getId() == null) {
            result.rejectValue("brand", "required", "Please select a brand");
        }
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            result.rejectValue("category", "required", "Please select a category");
        }
    }

    private void addFormOptions(Model model) {
        model.addAttribute("platforms", Platform.values());
        model.addAttribute("ageRatings", AgeRating.values());
        model.addAttribute("brands", productService.findAllBrands());
        model.addAttribute("categories", productService.findAllCategories());
    }
}