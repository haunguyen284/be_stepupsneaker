package com.ndt.be_stepupsneaker.util;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class ConvertUtil {
    public static Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Long epochMillis = instant.toEpochMilli();
        return epochMillis;
    }

    public static LocalDateTime convertLongToLocalDateTime(Long longTime) {
        LocalDateTime localDateTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(longTime), TimeZone.getDefault().toZoneId());
        return localDateTime;
    }

    public static String convertFloatToVnd(float amount) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(locale);
        double amountInDouble = (double) amount;
        String formattedAmount = currencyVN.format(amountInDouble);
        formattedAmount = formattedAmount.replace("đ", " VNĐ");
        return formattedAmount;
    }
}
