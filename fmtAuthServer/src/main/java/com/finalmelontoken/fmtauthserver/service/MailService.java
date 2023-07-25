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

    public void sendSimpleMessage(Mail data) {

        System.out.println(data);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(data.getEmail());
        message.setSubject(data.getName());
        message.setText(data.getMessage());
        emailSender.send(message);
    }
}
