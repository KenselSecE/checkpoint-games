package com.checkpointgames.checkpoint_games.config;

import com.checkpointgames.checkpoint_games.model.Role;
import com.checkpointgames.checkpoint_games.model.User;
import com.checkpointgames.checkpoint_games.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createIfMissing("admin", "admin@checkpointgames.test", "Admin User", "Admin123!", Role.ADMIN);
        createIfMissing("staff", "staff@checkpointgames.test", "Staff User", "Staff123!", Role.STAFF);
        createIfMissing("customer", "customer@checkpointgames.test", "Customer User", "Customer123!", Role.CUSTOMER);
    }

    private void createIfMissing(String username, String email, String fullName, String rawPassword, Role role) {
        if (userRepository.existsByUsername(username)) {
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }
}