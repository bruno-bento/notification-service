package com.brunob.notification_service.domain.repository;

import com.brunob.notification_service.domain.model.notification.Notification;
import java.util.Optional;
import java.util.List;

public interface NotificationRepository<T extends Notification> {
    T save(T notification);
    Optional<T> findById(Long id);
    List<T> findAll();
}