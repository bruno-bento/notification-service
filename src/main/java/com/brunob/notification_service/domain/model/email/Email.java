package com.brunob.notification_service.domain.model.email;

import com.brunob.notification_service.domain.model.notification.Notification;
import com.brunob.notification_service.domain.model.notification.NotificationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Getter
@NoArgsConstructor
public class Email extends Notification {
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;

    @Builder
    public Email(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.status = NotificationStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    @Override
    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
        this.retryCount++;
    }

    @Override
    public boolean canRetry() {
        return (this.status == NotificationStatus.PENDING ||
                this.status == NotificationStatus.FAILED) &&
                this.retryCount < 3;
    }
}