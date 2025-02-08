package com.brunob.notification_service.infrastructure.mail;

import com.brunob.notification_service.domain.model.email.Email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    private  JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;
    public void send(Email email) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailProperties.getFrom());
        helper.setTo(email.getRecipient());
        helper.setSubject(email.getSubject());
        helper.setText(email.getBody(), true);

        mailSender.send(message);
    }
}