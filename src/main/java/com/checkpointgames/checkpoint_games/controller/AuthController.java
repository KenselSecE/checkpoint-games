package com.checkpointgames.checkpoint_games.controller;

import com.checkpointgames.checkpoint_games.model.RegistrationForm;
import com.checkpointgames.checkpoint_games.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                           BindingResult result) {
        if (form.getPassword() != null && !form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (form.getUsername() != null && userService.usernameExists(form.getUsername())) {
            result.rejectValue("username", "taken", "This username is already taken");
        }
        if (form.getEmail() != null && userService.emailExists(form.getEmail())) {
            result.rejectValue("email", "taken", "This email is already registered");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.register(form);
        return "redirect:/login?registered";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}