package com.darkona.toolset.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtilities {


    private DateUtilities(){}

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_NO_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_NO_TIMEZONE_SHORT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_DATE_HUMAN = "yyyy/MM/dd";

    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
    public static final DateTimeFormatter SHORT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_NO_TIMEZONE = DateTimeFormatter.ofPattern(DATE_FORMAT_NO_TIMEZONE);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_NO_TIMEZONE_SHORT = DateTimeFormatter.ofPattern(DATE_FORMAT_NO_TIMEZONE_SHORT);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_DATE_HUMAN = DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_HUMAN);

    public static ZonedDateTime getTimestamp() {
        return ZonedDateTime.ofInstant(Instant.now(Clock.systemUTC()), Clock.systemUTC().getZone());
    }

    public static String getTimestampString(){
        return getTimestamp().format(TIMESTAMP_FORMATTER);
    }



}
