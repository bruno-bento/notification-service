package com.brunob.notification_service.domain.repository;

import com.brunob.notification_service.domain.model.Role;
import com.brunob.notification_service.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByRole(Role role);
}
