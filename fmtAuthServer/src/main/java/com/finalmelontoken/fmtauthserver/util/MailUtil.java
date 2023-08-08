package com.finalmelontoken.fmtauthserver.util;

import com.finalmelontoken.fmtauthserver.domain.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender emailSender;

    @Async
    public void sendMessage(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getEmail());
        message.setSubject(mail.getName());
        message.setText(mail.getMessage());
        emailSender.send(message);
    }

    public boolean isExpiredMail(Instant instant) {
        Instant now = Instant.now();

        Duration duration = Duration.between(instant, now);
        return duration.toMinutes() >= 5;
    }

    public boolean isSchoolEmail(String email) {
        return email.split("@")[1].equals("dgsw.hs.kr");
    }

    public boolean isToday(Instant instant) {
        LocalDate currentDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        return currentDate.equals(today);
    }
}
