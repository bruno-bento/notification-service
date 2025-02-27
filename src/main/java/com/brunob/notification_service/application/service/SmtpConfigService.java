package com.brunob.notification_service.application.service;

import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import com.brunob.notification_service.domain.repository.EmailRepository;
import com.brunob.notification_service.domain.repository.SmtpConfigRepository;
import com.brunob.notification_service.presentation.dto.SmtpConfigRequestDTO;
import com.brunob.notification_service.presentation.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SmtpConfigService {
    @Autowired
    private SmtpConfigRepository repository;

    @Autowired
    private EmailRepository emailRepository;

    public List<SmtpConfig> listAll() {
        return repository.findAll();
    }

    public SmtpConfig getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SMTP com ID " + id + " não encontrado"));
    }

    public SmtpConfig chooseLeastUsedSmtp() {
        List<SmtpConfig> allSmtps = repository.findAll();

        if (allSmtps.isEmpty()) {
            throw new RuntimeException("Nenhum servidor SMTP configurado");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        Map<Long, Long> smtpUsageCounts = emailRepository.countSentEmailsBySmtpIdBetween(startOfDay, endOfDay);

        return allSmtps.stream()
                .min((s1, s2) -> {
                    long count1 = smtpUsageCounts.getOrDefault(s1.getId(), 0L);
                    long count2 = smtpUsageCounts.getOrDefault(s2.getId(), 0L);

                    double ratio1 = (double) count1 / s1.getDailyLimit();
                    double ratio2 = (double) count2 / s2.getDailyLimit();

                    return Double.compare(ratio1, ratio2);
                })
                .orElseThrow(() -> new RuntimeException("Erro ao escolher servidor SMTP"));
    }

    public SmtpConfig create(SmtpConfigRequestDTO request) {
        SmtpConfig config = SmtpConfig.builder()
                .host(request.getHost())
                .port(request.getPort())
                .username(request.getUsername())
                .password(request.getPassword())
                .useTls(request.getUseTls())
                .useSsl(request.getUseSsl())
                .dailyLimit(request.getDailyLimit())
                .monthlyLimit(request.getMonthlyLimit())
                .build();

        return repository.save(config);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean isSmtpValid(Long smtpId) {
        if (repository.count() == 0) {
            throw new IllegalStateException("Nenhum servidor SMTP configurado");
        }

        if (smtpId == null) return true;

        return repository.existsById(smtpId);
    }

}
