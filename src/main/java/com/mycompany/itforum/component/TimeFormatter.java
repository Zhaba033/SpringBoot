package com.mycompany.itforum.component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;


@Component
public class TimeFormatter {

    public String fromNow(LocalDateTime time) {
        PrettyTime prettyTime = new PrettyTime(new Date());
        return prettyTime.format(
            Date.from(time.atZone(ZoneId.systemDefault()).toInstant())
        );
    }
}
