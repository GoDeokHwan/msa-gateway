package com.example.servicea.global.util.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateTimeUtil {

    public static LocalDateTime kstTimeNot() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return zonedDateTime.toLocalDateTime();
    }
}
