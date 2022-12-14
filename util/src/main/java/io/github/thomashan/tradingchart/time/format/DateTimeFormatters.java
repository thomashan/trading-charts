package io.github.thomashan.tradingchart.time.format;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public interface DateTimeFormatters {
    DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .appendZoneOrOffsetId()
            .toFormatter();
}
