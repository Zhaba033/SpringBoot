package com.mycompany.itforum.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;


@Component
public class TimeFormatter {

    public static String fromNow(LocalDateTime time) {
        PrettyTime prettyTime = new PrettyTime(new Date());
        return prettyTime.format(
            Date.from(time.atZone(ZoneId.systemDefault()).toInstant())
        );
    }
    
    public static String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return time.format(formatter);
    }
    
    public static String formatTime(LocalDateTime time, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return time.format(formatter);
    }
}
