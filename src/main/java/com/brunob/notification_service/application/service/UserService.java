package com.brunob.notification_service.application.service;

import com.brunob.notification_service.domain.model.Role;
import com.brunob.notification_service.domain.model.User;
import com.brunob.notification_service.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean adminExists() {
        return userRepository.existsByRole(Role.ADMIN);
    }
    public void setupAdmin(String username, String rawPassword) {
        if (adminExists()) {
            throw new IllegalStateException("Admin already exists");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User admin = User.createAdmin(username, encodedPassword);
        userRepository.save(admin);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


}