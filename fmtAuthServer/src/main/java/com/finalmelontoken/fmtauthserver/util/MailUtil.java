package com.finalmelontoken.fmtauthserver.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class MailUtil {

    public boolean isExpiredMail(Instant instant) {
        Instant now = Instant.now();

        Duration duration = Duration.between(instant, now);
        return duration.toMinutes() >= 5;
    }

    public boolean isSchoolEmail(String email) {
        return email.split("@")[1].equals("dgsw.hs.kr");
    }
}
