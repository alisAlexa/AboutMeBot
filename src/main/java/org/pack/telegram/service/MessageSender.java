package org.pack.telegram.service;

import org.pack.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static org.pack.telegram.enums.MeetingEnum.PREV_WEEK;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.service.ButtonService.backButton;
import static org.pack.telegram.service.CalendarService.getCalendar;

/**
 * Класс отвечает за отправку сообщений в бот
 */
@Component
public class MessageSender {
    private static final Logger log = LoggerFactory.getLogger(MessageSender.class);
    private final TelegramBot bot;

    @Autowired
    public MessageSender(@Lazy TelegramBot bot) {
        this.bot = bot;
    }

    public void deleteMessage(String chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(chatId));
        deleteMessage.setMessageId(messageId);

        try {
            bot.execute(deleteMessage);
        } catch (TelegramApiException exception) {
            log.error("Error delete message with messageId: {}, text error: {}", messageId, exception.getMessage());
        }
    }

    public void sendMessage(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error send message to telegram : {}", exception.getMessage());
        }
    }

    public void sendMessageAndSaveIDMessage(Map<String, Integer> lastMessageIds,  SendMessage message) {
        Message sendMessage = new Message();
        try {
            sendMessage = bot.execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error send message to telegram : {}", exception.getMessage());
        }
        lastMessageIds.put(String.valueOf(sendMessage.getChatId()), sendMessage.getMessageId());
    }

    public SendMessage fillMeetingMessage(String chatId, String callbackData) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Запись на встречу: ");
        if (callbackData.equals(String.valueOf(MEETING)) ||
                callbackData.equals(String.valueOf(PREV_WEEK))) {
            message.setReplyMarkup(getCalendar(false));
        } else {
            message.setReplyMarkup(getCalendar(true));
        }

        return message;
    }


    public SendMessage fillTextMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(backButton());
        return message;
    }

}
