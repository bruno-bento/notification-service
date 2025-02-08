package com.brunob.notification_service.infrastructure.persistence;

import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepositoryImpl
        extends JpaRepository<Email, Long>, NotificationRepository<Email> {
}