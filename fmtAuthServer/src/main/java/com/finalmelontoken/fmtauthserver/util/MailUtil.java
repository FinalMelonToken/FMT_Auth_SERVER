package com.finalmelontoken.fmtauthserver.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class MailUtil {

    public boolean isExpiredMail(LocalDate localDate) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime targetDateTime = localDate.atStartOfDay();
        Duration duration = Duration.between(targetDateTime, now);
        return duration.toMinutes() >= 5;
    }
}
