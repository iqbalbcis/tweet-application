package com.main.util;

import com.microsoft.azure.servicebus.primitives.StringUtil;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import static com.main.constants.CommonConstant.TOKEN_TIMEOUT_IN_MINUTES;

@Component
public class DateUtil {

    public static final String localTimeZone() {
        return ZoneId.systemDefault().toString();
    }

    public long getCurrentTimeInMillis() {
        switch (Locale.getDefault().getDisplayCountry()) {
            case "United Kingdom":
                return currentTimeMillisForTimeZone("Europe/London");
            case "United States":
                return currentTimeMillisForTimeZone("America/Washington");
            default:
                return System.currentTimeMillis();
        }
    }

    private long currentTimeMillisForTimeZone(String timeZone) {
        return LocalDateTime.now().atZone(ZoneId.of(timeZone)).toInstant().toEpochMilli();
        //return Calendar.getInstance(TimeZone.getTimeZone(timeZone)).getTime().getTime();
    }


    public Timestamp getTokenExpiredTime() {
        switch (Locale.getDefault().getDisplayCountry()) {
            case "United Kingdom":
                return tokenExpiredTimeForTimeZone("Europe/London");
            case "United States":
                return tokenExpiredTimeForTimeZone("America/Washington");
            default:
                return tokenExpiredTimeForTimeZone(null);
        }
    }

    private Timestamp tokenExpiredTimeForTimeZone(String timeZone) {
        if(StringUtil.isNullOrEmpty(timeZone)) {
            return Timestamp.valueOf(LocalDateTime.now().plusMinutes(TOKEN_TIMEOUT_IN_MINUTES));
        }
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of(timeZone))
                .plusMinutes(TOKEN_TIMEOUT_IN_MINUTES));
    }

    public Timestamp getCurrentTimeStampForTimeZone() {
        switch (Locale.getDefault().getDisplayCountry()) {
            case "United Kingdom":
                return currentTimeStampForTimeZone("Europe/London");
            case "United States":
                return currentTimeStampForTimeZone("America/Washington");
            default:
                return Timestamp.valueOf(LocalDateTime.now());
        }
    }

    public Timestamp currentTimeStampForTimeZone(String timeZone) {
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of(timeZone)));
    }

    public Date getCurrentDateForTimeZone() {
        switch (Locale.getDefault().getDisplayCountry()) {
            case "United Kingdom":
                return currentDateForTimeZone("Europe/London");
            case "United States":
                return currentDateForTimeZone("America/Washington");
            default:
                return Date.valueOf(LocalDate.now());
        }
    }

    private Date currentDateForTimeZone(String timeZone) {
        return Date.valueOf(LocalDate.now(ZoneId.of(timeZone)));
    }

    private Date convertInDate(String date) { // date pattern dd/MM/yyyy
        //TemporalAccessor parse = DateTimeFormatter.ofPattern("dd/MM/yyyy").parse(date);
        return Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    // ---
    public Timestamp getCurrentTimeStampForUtcTimeZone() {
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
    }

    public Timestamp convertTimeStampForUtcTimeZone(Timestamp timeStamp) {
        return Timestamp.valueOf(timeStamp.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
    }

    public java.util.Date convertStringDate(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }

}
