package org.pack.telegram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.pack.telegram.enums.MeetingEnum.*;
import static org.pack.telegram.service.ButtonService.getBackButton;
import static org.pack.telegram.util.CalendarUtils.*;
import static org.pack.telegram.util.StringExtractor.capitalizeFirstLetter;

@Component
public class CalendarService {
    private static final Logger log = LoggerFactory.getLogger(CalendarService.class);

    /**
     * Этот метод создаёт и возвращает объект InlineKeyboardMarkup,
     * который представляет собой календарь с инлайн-кнопками.
     * Он принимает булевый параметр isNextWeek, который определяет,
     * нужно ли показывать текущую или следующую неделю.
     * @param isNextWeek
     * @return InlineKeyboardMarkup с кнопками календаря.
     */
    public static InlineKeyboardMarkup getCalendar(boolean isNextWeek) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Получаем будние дни
        List<LocalDate> weekdays = getWeekdays(isNextWeek);

        // Создаём кнопки для дней недели
        for (LocalDate day : weekdays) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            String dayOfWeekText = capitalizeFirstLetter(day.format(DAY_OF_WEEK_FORMATTER)); // "Понедельник"
            String dayOfMonthText = day.format(DAY_OF_MONTH_FORMATTER); // "4"
            String monthText = capitalizeFirstLetter(day.format(MONTH_FORMATTER)); // "Апреля"

            button.setText(String.format("%s %s %s", dayOfWeekText, dayOfMonthText, monthText)); // "Понедельник 4 Апреля"
            button.setCallbackData(String.format("DATE_%s_%s_%s", dayOfWeekText.toUpperCase(), dayOfMonthText, monthText.toUpperCase()));

            rowInline.add(button);
            rowsInline.add(rowInline);
        }

        // Добавляем кнопки для переключения недель и возврата в меню
        rowsInline.add(createWeekSwitchButtons());
        rowsInline.add(getBackButton());

        markup.setKeyboard(rowsInline);
        return markup;
    }

    /**
     * Этот вспомогательный метод создаёт и возвращает
     * список из двух InlineKeyboardButton: одна для перехода
     * на предыдущую неделю, другая — на следующую.
     * @return List<InlineKeyboardButton>
     */
    private static List<InlineKeyboardButton> createWeekSwitchButtons() {
        List<InlineKeyboardButton> weekSwitchButtons = new ArrayList<>();

        InlineKeyboardButton previousWeekButton = new InlineKeyboardButton();
        previousWeekButton.setText("Предыдущая неделя");
        previousWeekButton.setCallbackData(PREV_WEEK.name());//Сюда можно только в том случае если переключали на следующую

        InlineKeyboardButton nextWeekButton = new InlineKeyboardButton();
        nextWeekButton.setText("Следующая неделя");
        nextWeekButton.setCallbackData(NEXT_WEEK.name());

        weekSwitchButtons.add(previousWeekButton);
        weekSwitchButtons.add(nextWeekButton);

        return weekSwitchButtons;
    }

    /**
     * Этот метод предоставляет функционал для получения списка будних дней
     * (понедельник-пятница) для текущей или следующей недели.
     * @param nextWeek - если false, то выдает текущую, если true следующую
     * @return List<LocalDate> - будние дни
     */
    public static List<LocalDate> getWeekdays(boolean nextWeek) {
        LocalDate start = LocalDate.now();
        if (nextWeek) {
            start = start.plusWeeks(1);
        }

        List<LocalDate> weekdays = new ArrayList<>();
        LocalDate monday = start.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 5; i++) {
            weekdays.add(monday.plusDays(i));
        }

        return weekdays;
    }
}
