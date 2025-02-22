package com.brunob.notification_service.application.service;

import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import com.brunob.notification_service.domain.repository.SmtpConfigRepository;
import com.brunob.notification_service.presentation.dto.SmtpConfigRequestDTO;
import com.brunob.notification_service.presentation.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmtpConfigService {
    @Autowired
    private SmtpConfigRepository repository;

    public List<SmtpConfig> listAll() {
        return repository.findAll();
    }

    public SmtpConfig getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SMTP com ID " + id + " não encontrado"));
    }

    public SmtpConfig chooseLeastUsedSmtp() {
        return repository.findAll().stream()
                .min((s1, s2) -> Integer.compare(s1.getDailyLimit(), s2.getDailyLimit()))
                .orElseThrow(() -> new RuntimeException("Nenhum servidor SMTP configurado"));
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
