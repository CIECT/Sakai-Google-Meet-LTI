package coza.opencollab.meetings.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import coza.opencollab.meetings.constant.Action;

@Component
public class StringToActionConverter implements Converter<String, Action> {


    @Override
    public Action convert(String param) {
        return Action.getInstance(param);
    }
}
