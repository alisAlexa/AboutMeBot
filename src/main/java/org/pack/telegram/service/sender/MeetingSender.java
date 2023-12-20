package org.pack.telegram.service.sender;

import org.pack.telegram.entity.Meeting;
import org.pack.telegram.entity.User;
import org.pack.telegram.service.MeetingService;
import org.pack.telegram.service.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.pack.telegram.enums.MeetingEnum.APPROVE;
import static org.pack.telegram.enums.MeetingEnum.PREV_WEEK;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.enums.MenuButtonsEnum.MENU;
import static org.pack.telegram.service.ButtonService.createButtonMenu;
import static org.pack.telegram.service.ButtonService.getBackButton;
import static org.pack.telegram.service.CalendarService.getCalendar;
import static org.pack.telegram.service.TimeService.getBackToDate;
import static org.pack.telegram.service.TimeService.getTime;

@Component
public class MeetingSender {
    private static final Logger log = LoggerFactory.getLogger(MeetingSender.class);
    private final MeetingService meetingService;

    private final MessageSender sender;


    @Autowired
    public MeetingSender(MeetingService meetingService, MessageSender sender) {
        this.meetingService = meetingService;
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

        // Если все слоты заняты (4 занятых слота), показываем сообщение об отсутствии доступного времени
        if (occupiedSlots.size() == 4) {//TODO
            message.setText("К сожалению нет доступного времени, вернитесь к дате и выберите другой день, либо вернитесь в меню");
            message.setReplyMarkup(getBackToDate());
        } else {
            // В противном случае (меньше 4 занятых слотов), показываем доступные временные слоты
            message.setText("Выберите, пожалуйста, время: ");
            message.setReplyMarkup(getTime(occupiedSlots));
        }

        sender.sendMessage(message);
    }

    public void confirmationOfTheMeeting(User user) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(user.getChatId()));

        Meeting meeting = meetingService.getMeetingByChatId(user.getChatId());

        message.setText("Подтвердите дату и время встречи :" +
                meeting.getMeeting()); //TODO скорректировать формат отправки даты


        message.setReplyMarkup(getKeyboardApproval());

        sender.sendMessage(message);
    }

    private InlineKeyboardMarkup getKeyboardApproval() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> nextLine = new ArrayList<>();

        nextLine.add(createButtonMenu("Подтвердждаю", String.valueOf(APPROVE)));
        rowsInline.add(nextLine);
        rowsInline.add(getBackButton());

        markup.setKeyboard(rowsInline);
        return markup;
    }
}
