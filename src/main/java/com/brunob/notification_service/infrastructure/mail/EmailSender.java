package com.brunob.notification_service.infrastructure.mail;

import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailSender {
    @Autowired
    private  JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Value("${app.tracking.pixel-url}")
    private String trackingPixelBaseUrl;

    public void send(Email email, SmtpConfig smtpConfig) throws Exception {
        JavaMailSender mailSender = createMailSender(smtpConfig);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailProperties.getFrom());
        helper.setTo(email.getRecipient());
        helper.setSubject(email.getSubject());

        String trackingPixel = String.format(
                "<img src='%s/api/track/%s' width='1' height='1' />",
                trackingPixelBaseUrl,
                email.getTrackingId()
        );

        String bodyWithTracking = email.getBody() + trackingPixel;

        helper.setText(bodyWithTracking, true);

        //Bouce settings
        message.setHeader("Return-Path", mailProperties.getFrom());
        message.setHeader("X-Track-ID", email.getTrackingId());

        mailSender.send(message);
    }

    private JavaMailSender createMailSender(SmtpConfig smtpConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpConfig.getHost());
        mailSender.setPort(smtpConfig.getPort());
        mailSender.setUsername(smtpConfig.getUsername());
        mailSender.setPassword(smtpConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}