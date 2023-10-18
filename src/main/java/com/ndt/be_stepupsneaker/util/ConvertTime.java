package com.ndt.be_stepupsneaker.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ConvertTime {
    public static Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Long epochMillis = instant.toEpochMilli();
        return epochMillis;
    }
}
