package com.checkpointgames.checkpoint_games.controller;

import com.checkpointgames.checkpoint_games.model.Role;
import com.checkpointgames.checkpoint_games.model.User;
import com.checkpointgames.checkpoint_games.service.ProductService;
import com.checkpointgames.checkpoint_games.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final UserService userService;

    public AdminController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("productCount", productService.count());
        model.addAttribute("userCount", userService.count());
        return "admin/dashboard";
    }

    // Products:
    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        productService.delete(id);
        ra.addFlashAttribute("successMessage", "Product deleted.");
        return "redirect:/admin/products";
    }

    // Users:
    @GetMapping("/users")
    public String users(@AuthenticationPrincipal User current, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        model.addAttribute("currentUserId", current.getId());
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role,
                             @AuthenticationPrincipal User current, RedirectAttributes ra) {
        if (current.getId().equals(id)) {
            ra.addFlashAttribute("errorMessage", "You cannot change your own role.");
        } else {
            userService.changeRole(id, role);
            ra.addFlashAttribute("successMessage", "Role updated.");
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, @RequestParam boolean enable,
                             @AuthenticationPrincipal User current, RedirectAttributes ra) {
        if (current.getId().equals(id)) {
            ra.addFlashAttribute("errorMessage", "You cannot deactivate your own account.");
        } else {
            userService.setEnabled(id, enable);
            ra.addFlashAttribute("successMessage", enable ? "Account activated." : "Account deactivated.");
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, @AuthenticationPrincipal User current, RedirectAttributes ra) {
        if (current.getId().equals(id)) {
            ra.addFlashAttribute("errorMessage", "You cannot delete your own account.");
        } else {
            userService.delete(id);
            ra.addFlashAttribute("successMessage", "User deleted.");
        }
        return "redirect:/admin/users";
    }
}