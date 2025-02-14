package com.brunob.notification_service.domain.repository;

import com.brunob.notification_service.domain.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface NotificationRepository<T extends Notification> extends JpaRepository<T, Long> {
    Optional<T> findById(Long id);
    List<T> findAll();
}