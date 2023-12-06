package org.pack.telegram;

import org.pack.telegram.config.BotConfig;
import org.pack.telegram.service.CalendarService;
import org.pack.telegram.service.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.pack.telegram.enums.MenuButton.*;
import static org.pack.telegram.service.ButtonService.*;
import static org.pack.telegram.service.CalendarService.getCalendar;
import static org.pack.telegram.service.CalendarService.getWeekdays;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);


    private final BotConfig botConfig;
    private final MessageSender sender;
    @Autowired
    public TelegramBot(BotConfig botConfig, MessageSender sender) {
        this.botConfig = botConfig;
        this.sender = sender;
        log.info("TelegramBot worked");
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = String.valueOf(update.getMessage().getChatId());
            mainMenu(chatId);
        }

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            int messageId = update.getCallbackQuery().getMessage().getMessageId();


            if (callbackData.equals(String.valueOf(WORK_EXPERIENCE))) {
                sender.deleteMessage(chatId, messageId);

                sender.sendMessage(sender.fillTextMessage(chatId, "Мой опыт работы включает в себя..."));
            } else if (callbackData.equals(String.valueOf(TECHNICAL_SKILLS))) {
                sender.deleteMessage(chatId, messageId);

                sender.sendMessage(sender.fillTextMessage(chatId, "Мои технические навыки..."));
            } else if (callbackData.equals(String.valueOf(MEETING)) ||
                        callbackData.equals(String.valueOf(NEXT_WEEK)) ||
                        callbackData.equals(String.valueOf(PREV_WEEK))) {
                sender.deleteMessage(chatId, messageId);
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Запись на встречу: ");
                if (callbackData.equals(String.valueOf(MEETING)) ||
                        callbackData.equals(String.valueOf(PREV_WEEK))) {
                    message.setReplyMarkup(getCalendar(false));
                } else {
                    message.setReplyMarkup(getCalendar(true));
                }
                sender.sendMessage(message);
            } else if (callbackData.equals(String.valueOf(MENU))) {
                sender.deleteMessage(chatId, messageId);

                mainMenu(chatId);
            }
        }
    }


    /**
     * Метод отвечает за главное меню
     * @param chatId
     */
    private void mainMenu(String chatId) {
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


    @Override
    public String getBotUsername() {
        return botConfig.getNameBot();
    }

    @Override
    public String getBotToken() {
        return botConfig.getTokenBot();
    }

}
