package org.pack.telegram.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalendarUtils {

    public static final DateTimeFormatter DAY_OF_WEEK_FORMATTER = DateTimeFormatter.ofPattern("EEEE", new Locale("ru", "RU"));
    public static final DateTimeFormatter DAY_OF_MONTH_FORMATTER = DateTimeFormatter.ofPattern("d", new Locale("ru", "RU"));
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM", new Locale("ru", "RU"));


}
