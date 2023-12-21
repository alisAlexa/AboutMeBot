package org.pack.telegram.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.pack.telegram.enums.MeetingEnum.DECLINE;
import static org.pack.telegram.enums.MeetingEnum.MEETING;
import static org.pack.telegram.enums.MenuButtonsEnum.MENU;

@Component
public class ButtonService {

    private final static String BACK_TO_MENU = "Вернуться в главное меню \uD83D\uDD19";

    /**
     * Метод создает кнопку у сообщения, для возврата в меню
     * работает как клава, то есть использовать только в одном случае
     * когда надо вернуться в меню и все
     * @return InlineKeyboardMarkup
     */
    public static InlineKeyboardMarkup backButtonKeyboard() {
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText(BACK_TO_MENU);
        backButton.setCallbackData(String.valueOf(MENU));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(backButton);
        rows.add(row);
        keyboardMarkup.setKeyboard(rows);

        return keyboardMarkup;
    }

    /**
     * Метод создает кнопку у сообщения, для возврата в меню
     * как кнопка, которую надо вставить в InlineKeyboardMarkup
     * будет на отдельном уровне
     * @return InlineKeyboardButton
     */
    public static List<InlineKeyboardButton> getBackButton() {
        List<InlineKeyboardButton> getBackMenu = new ArrayList<>();

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText(BACK_TO_MENU);
        backButton.setCallbackData(String.valueOf(MENU));

        getBackMenu.add(backButton);

        return getBackMenu;
    }

    public static List<InlineKeyboardButton> getBackToCalendarButton() {
        List<InlineKeyboardButton> getBackMenu = new ArrayList<>();

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Выбрать другую дату");
        backButton.setCallbackData(MEETING.name() + DECLINE.name());

        getBackMenu.add(backButton);

        return getBackMenu;
    }

    /**
     * Метод отвечает за создание кнопок для меню
     * @param text - текст который будет выведен на кнопке
     * @param callbackData - для идентификации ботом нужных команд
     * @return InlineKeyboardButton
     */
    public static InlineKeyboardButton createButtonMenu(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }


}
