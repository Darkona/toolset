package com.darkona.toolset.mongo;

import com.darkona.toolset.time.DateUtilities;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;

public class ZonedDateTimeWritingConverter implements Converter<ZonedDateTime, String> {

    @Override
    @NotNull
    public String convert(@NotNull ZonedDateTime source) {
        return source.format(DateUtilities.TIMESTAMP_FORMATTER);
    }
}
