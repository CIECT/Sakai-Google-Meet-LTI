package coza.opencollab.meetings.utils;

import java.net.URI;

import lombok.NonNull;

public class Redirect {


    public static final String PREFIX = "redirect:";


    public static String to(@NonNull String location) {
        return PREFIX + location;
    };

    public static String to(@NonNull URI uri) {
        return to(uri.toString());
    };
}
