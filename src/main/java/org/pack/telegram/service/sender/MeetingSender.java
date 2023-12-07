package org.pack.telegram.service.sender;

import org.pack.telegram.TelegramBot;
import org.pack.telegram.service.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.pack.telegram.enums.MeetingEnum.PREV_WEEK;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.service.CalendarService.getCalendar;

@Component
public class MeetingSender {
    private static final Logger log = LoggerFactory.getLogger(MeetingSender.class);
    private final TelegramBot bot;

    @Autowired
    public MeetingSender(@Lazy TelegramBot bot) {
        this.bot = bot;
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
}
