package com.checkpointgames.checkpoint_games.service;

import com.checkpointgames.checkpoint_games.model.RegistrationForm;
import com.checkpointgames.checkpoint_games.model.Role;
import com.checkpointgames.checkpoint_games.model.User;
import com.checkpointgames.checkpoint_games.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Public registration always creates a CUSTOMER with an encoded password
    public User register(RegistrationForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setFullName(form.getFullName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.CUSTOMER);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }

    public long count() {
        return userRepository.count();
    }

    public void changeRole(Long id, Role role) {
        User user = getUser(id);
        user.setRole(role);
        userRepository.save(user);
    }

    public void setEnabled(Long id, boolean enabled) {
        User user = getUser(id);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.delete(getUser(id));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}