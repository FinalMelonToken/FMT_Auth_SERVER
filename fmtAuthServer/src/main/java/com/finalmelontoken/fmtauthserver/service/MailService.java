package com.finalmelontoken.fmtauthserver.service;

import com.finalmelontoken.fmtauthserver.domain.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    public void sendMessage(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getEmail());
        message.setSubject(mail.getName());
        message.setText(mail.getMessage());
        emailSender.send(message);
    }
}
