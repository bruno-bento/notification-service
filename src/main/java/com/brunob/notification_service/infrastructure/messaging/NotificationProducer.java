package com.brunob.notification_service.infrastructure.messaging;

import com.brunob.notification_service.domain.model.notification.Notification;

public interface NotificationProducer <T extends Notification> {
    void sendToQueue(Long notificationId);
}