package org.pack.telegram.service;

import org.pack.telegram.service.sender.MeetingSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.pack.telegram.service.ButtonService.getBackButton;
import static org.pack.telegram.service.ButtonService.getBackToCalendarButton;

@Component
public class TimeService {

    private static final Logger log = LoggerFactory.getLogger(TimeService.class);

    /**
     * Создает инлайн-клавиатуру с доступными временными слотами.
     *
     * @param occupiedTimes Список занятых временных слотов.
     * @return InlineKeyboardMarkup с кнопками для свободных временных слотов.
     */
    public static InlineKeyboardMarkup getTime(Set<String> occupiedTimes) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<String> allTimes = Arrays.asList("12:00", "14:00", "16:00", "18:00");
        for (String time : allTimes) {
            if (!occupiedTimes.contains(time)) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(time);
                button.setCallbackData("TIME_" + time);
                rowInline.add(button);
                rowsInline.add(rowInline);
            }
        }

        rowsInline.add(getBackButton());

        markup.setKeyboard(rowsInline);
        return markup;
    }

    public static InlineKeyboardMarkup getBackToDate() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        rowsInline.add(getBackToCalendarButton());
        rowsInline.add(getBackButton());

        markup.setKeyboard(rowsInline);
        return markup;
    }


    /**
     * Извлекает время в формате LocalTime из строки.
     *
     * @param input Строка в формате "TIME_HH:MM", например "TIME_12:00".
     * @return Время в формате LocalTime или null, если строка некорректна.
     */
    public static LocalTime extractTime(String input) {
        try {
            String[] parts = input.split("_");
            if (parts.length > 1) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                return LocalTime.parse(parts[1], formatter);
            }
        } catch (DateTimeParseException e) {
            // Обработка исключения, если строка не соответствует формату времени
            log.error("Error parse date : {}", e.getMessage());

        }
        return null; // или выбросить исключение, в зависимости от требований
    }
}
