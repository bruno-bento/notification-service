package com.brunob.notification_service.infrastructure.mail;

import com.brunob.notification_service.domain.model.email.Email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    private  JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Value("${app.tracking.pixel-url}")
    private String trackingPixelBaseUrl;

    public void send(Email email) throws Exception {
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
}