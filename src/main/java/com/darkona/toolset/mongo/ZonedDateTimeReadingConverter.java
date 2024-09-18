package com.darkona.toolset.mongo;

import com.darkona.toolset.time.DateUtilities;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@ReadingConverter
public class ZonedDateTimeReadingConverter implements Converter<String, ZonedDateTime> {

    @Override
    @NotNull
    public ZonedDateTime convert(@NotNull String source) {

        try {
            return ZonedDateTime.parse(source, DateUtilities.TIMESTAMP_FORMATTER);
        } catch (DateTimeParseException ignore) {}

        try {
            return ZonedDateTime.parse(source, DateUtilities.SHORT_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ignore) {}

        try {
            return ZonedDateTime.parse(source, DateUtilities.DATE_TIME_FORMATTER_NO_TIMEZONE);
        } catch (DateTimeParseException ignore) {}
        return DateUtilities.getTimestamp();
    }
}
