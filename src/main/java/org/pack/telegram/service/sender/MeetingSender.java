package org.pack.telegram.service.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalTime;
import java.util.Set;

import static org.pack.telegram.enums.MeetingEnum.PREV_WEEK;
import static org.pack.telegram.enums.MeetingEnum.TIME;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.service.CalendarService.getCalendar;
import static org.pack.telegram.service.TimeService.getTime;

@Component
public class MeetingSender {
    private static final Logger log = LoggerFactory.getLogger(MeetingSender.class);
    private final MainSender sender;


    @Autowired
    public MeetingSender(MainSender sender) {
        this.sender = sender;
    }



    public void fillMeetingButtons (String chatId, String callbackData) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Запись на встречу, выберите день: ");
        if (callbackData.equals(String.valueOf(MEETING)) ||
                callbackData.equals(String.valueOf(PREV_WEEK))) {
            message.setReplyMarkup(getCalendar(false));
        } else {
            message.setReplyMarkup(getCalendar(true));
        }

        sender.sendMessage(message);
    }

    public void fillTimeButtons (String chatId, String callbackData, Set<String> occupiedSlots) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите пожалуйста время: ");

        message.setReplyMarkup(getTime(occupiedSlots));

        sender.sendMessage(message);
    }
}
