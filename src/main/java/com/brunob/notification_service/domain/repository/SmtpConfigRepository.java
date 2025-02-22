package com.brunob.notification_service.domain.repository;

import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmtpConfigRepository extends JpaRepository<SmtpConfig, Long> {
}