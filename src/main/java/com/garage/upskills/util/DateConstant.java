package com.garage.upskills.util;

import java.time.format.DateTimeFormatter;

public interface DateConstant {

    String DATE_TIME_FORMAT = "yyyy-MM-dd";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
}
