package com.brunob.notification_service.application.service;

import com.brunob.notification_service.domain.model.notification.Notification;

import java.util.List;

public interface NotificationService<T extends Notification> {
    T create(Object request);
    void send(Long id);
    List<T> listAll();
}