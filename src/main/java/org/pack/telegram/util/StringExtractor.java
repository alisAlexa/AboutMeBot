package org.pack.telegram.util;

import org.springframework.stereotype.Component;

@Component
public class StringExtractor {

    /**
     * Извлекает день недели из строки.
     *
     * @param input Строка в формате "DATE_DAYOFWEEK_NUMBER", например "DATE_TUESDAY_5".
     * @return День недели или null, если строка некорректна.
     */
    public static String extractDayOfWeek(String input) {
        String[] parts = input.split("_");
        if (parts.length > 2) {
            return parts[1];
        } else {
            return null; // или выбросить исключение, в зависимости от требований
        }
    }

    /**
     * Извлекает число из строки.
     *
     * @param input Строка в формате "DATE_DAYOFWEEK_NUMBER", например "DATE_TUESDAY_5".
     * @return Число или -1, если строка некорректна.
     */
    public static int extractDayOfMonth(String input) {
        String[] parts = input.split("_");
        if (parts.length > 2) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return -1; // или выбросить исключение, в зависимости от требований
            }
        } else {
            return -1; // или выбросить исключение, в зависимости от требований
        }
    }

}
