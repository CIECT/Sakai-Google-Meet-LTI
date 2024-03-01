package coza.opencollab.meetings.converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import coza.opencollab.meetings.constant.Web;

@Component
public class InstantToStringConverter implements Converter<Instant, String> {


    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Web.LOCAL_DATE_TIME_PATTERN);


    @Override
    public String convert(Instant instant) {
        // TODO: Use user time zone
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.from(instant.atZone(zoneId)).format(LOCAL_DATE_TIME_FORMATTER);
    }
}
