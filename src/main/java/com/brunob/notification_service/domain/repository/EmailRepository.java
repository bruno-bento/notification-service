package com.brunob.notification_service.domain.repository;

import com.brunob.notification_service.domain.model.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends NotificationRepository<Email> {
    Optional<Email> findByTrackingId(String trackingId);
}