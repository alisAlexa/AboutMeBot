package org.pack.telegram.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StringExtractor {
    private static final Logger log = LoggerFactory.getLogger(StringExtractor.class);


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
            log.error("Error extractDayOfWeek()");
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
                log.error("Error extractDayOfMonth(), message: {}", e.getMessage());
                return -1; // или выбросить исключение, в зависимости от требований
            }
        } else {
            log.error("Error extractDayOfMonth() with length parts");
            return -1; // или выбросить исключение, в зависимости от требований
        }
    }

    public static String extractMonth(String input) {
        String[] parts = input.split("_");
        if (parts.length > 2) {
            return parts[3];
        } else {
            log.error("Error extractDayOfWeek()");
            return null; // или выбросить исключение, в зависимости от требований
        }
    }


    public static Long createMeetingId(String input) {
        try {
            // Преобразование входной строки в Long
            long originalNumber = Long.parseLong(input);

            // Генерация трехзначного случайного числа
            Random random = new Random();
            int randomThreeDigits = 100 + random.nextInt(900);

            // Соединение чисел в одно
            String extendedNumberStr = Long.toString(originalNumber) + Integer.toString(randomThreeDigits);
            return Long.parseLong(extendedNumberStr);
        } catch (NumberFormatException e) {
             log.error("Error createMeetingId() create ID for Meeting text: {}", e.getMessage());
             return null; // или выбросить исключение
        }
    }

}
