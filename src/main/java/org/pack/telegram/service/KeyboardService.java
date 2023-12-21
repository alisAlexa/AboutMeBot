package org.pack.telegram.service;

import org.pack.telegram.service.sender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.pack.telegram.enums.MeetingEnum.DECLINE;
import static org.pack.telegram.enums.MenuButtonsEnum.*;
import static org.pack.telegram.service.ButtonService.createButtonMenu;

@Component
public class KeyboardService {

    private final MessageSender sender;

    @Autowired
    public KeyboardService(MessageSender sender) {
        this.sender = sender;
    }

    /**
     * Метод отвечает за главное меню
     * @param chatId
     */
    public void mainMenu(String chatId) {
        InlineKeyboardMarkup markup = getMainMenu();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Все что может Вас заинтересовать тут \uD83D\uDC47\uD83C\uDFFB");
        message.setReplyMarkup(markup);

        sender.sendMessage(message);
    }

    /**
     * Метод собирает клавиатуру для главного меню
     * @return markup
     */
    private static InlineKeyboardMarkup getMainMenu() {
        InlineKeyboardMarkup markup  = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInlineOne = new ArrayList<>();

        rowInline.add(createButtonMenu("Мой опыт работы",
                String.valueOf(WORK_EXPERIENCE)));

        rowInline.add(createButtonMenu("Мои технические навыки",
                String.valueOf(TECHNICAL_SKILLS)));

        rowsInline.add(rowInline);

        rowInlineOne.add(createButtonMenu("Записаться на встречу",
                String.valueOf(MEETING)));

        rowsInline.add(rowInlineOne);

        markup.setKeyboard(rowsInline);
        return markup;
    }

    public static List<InlineKeyboardButton> declineButtonInline(){
        List<InlineKeyboardButton> backLine = new ArrayList<>();
        backLine.add(createButtonMenu("Отменить запись и вернуться в меню", DECLINE.name()));
        return backLine;
    }
}
