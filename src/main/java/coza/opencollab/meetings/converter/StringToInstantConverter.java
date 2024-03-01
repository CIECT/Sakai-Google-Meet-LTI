package coza.opencollab.meetings.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import coza.opencollab.meetings.constant.Web;

@Component
public class StringToInstantConverter implements Converter<String, Instant> {


    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Web.LOCAL_DATE_TIME_PATTERN);


    @Override
    public Instant convert(String localTimeString) {
        // TODO: Use user time zone
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.parse(localTimeString, LOCAL_DATE_TIME_FORMATTER);

        ZoneOffset zoneOffset = zoneId.getRules().getOffset(localDateTime);

        return localDateTime.toInstant(zoneOffset);
    }
}
