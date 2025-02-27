package com.brunob.notification_service.domain.repository;

import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.model.notification.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EmailRepository extends NotificationRepository<Email> {
    Optional<Email> findByTrackingId(String trackingId);

    List<Email> findByStatusAndSentAtBetween(NotificationStatus status,
                                             LocalDateTime startDateTime,
                                             LocalDateTime endDateTime);

    // Método para contar emails enviados por SMTP
    @Query("SELECT e.smtpId, COUNT(e) FROM Email e WHERE e.status = 'SENT' " +
            "AND e.sentAt BETWEEN :startDate AND :endDate GROUP BY e.smtpId")
    List<Object[]> countSentEmailsBySmtpIdBetweenRaw(LocalDateTime startDate, LocalDateTime endDate);

    // Método helper para converter para Map
    default Map<Long, Long> countSentEmailsBySmtpIdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = countSentEmailsBySmtpIdBetweenRaw(startDate, endDate);
        Map<Long, Long> counts = new HashMap<>();

        for (Object[] result : results) {
            Long smtpId = (Long) result[0];
            Long count = ((Number) result[1]).longValue();
            counts.put(smtpId, count);
        }

        return counts;
    }
}