package org.pack.telegram.service.sender;

import org.pack.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MainSender {
    private static final Logger log = LoggerFactory.getLogger(MainSender.class);
    private final TelegramBot bot;

    @Autowired
    public MainSender (@Lazy TelegramBot bot) {
        this.bot = bot;
    }

    public void sendMessage(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException exception) {
            log.error("Error send message to telegram : {}", exception.getMessage());
        }
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
}
