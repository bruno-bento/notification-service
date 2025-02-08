package com.brunob.notification_service.domain.model.notification;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String recipient;

    @Enumerated(EnumType.STRING)
    protected NotificationStatus status;

    protected int retryCount;

    @CreationTimestamp
    protected LocalDateTime createdAt;

    protected LocalDateTime sentAt;

    public abstract void markAsSent();
    public abstract void markAsFailed();
    public abstract boolean canRetry();
}