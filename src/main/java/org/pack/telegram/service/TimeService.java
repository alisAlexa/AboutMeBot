package org.pack.telegram.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class TimeService {

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

        markup.setKeyboard(rowsInline);
        return markup;
    }

}
