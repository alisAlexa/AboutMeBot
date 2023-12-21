package org.pack.telegram.service.sender;

import org.pack.telegram.entity.Meeting;
import org.pack.telegram.entity.User;
import org.pack.telegram.service.KeyboardService;
import org.pack.telegram.service.MeetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.pack.telegram.enums.MeetingEnum.*;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.service.ButtonService.createButtonMenu;
import static org.pack.telegram.service.CalendarService.getCalendar;
import static org.pack.telegram.service.KeyboardService.declineButtonInline;
import static org.pack.telegram.service.TimeService.getBackToDate;
import static org.pack.telegram.service.TimeService.getTime;

@Component
public class MeetingSender {
    private static final Logger log = LoggerFactory.getLogger(MeetingSender.class);
    private final MeetingService meetingService;
    private final KeyboardService keyboard;
    private final MessageSender sender;


    @Autowired
    public MeetingSender(MeetingService meetingService, KeyboardService keyboard, MessageSender sender) {
        this.meetingService = meetingService;
        this.keyboard = keyboard;
        this.sender = sender;
    }

    public void handleMeetingInteraction(Meeting meeting, User user, String chatId, String callbackData) {
        // Преобразование chatId в Long один раз
        Long chatIdLong = Long.valueOf(chatId);

        boolean isMeeting = callbackData.contains(MEETING.name());

        if (callbackData.contains(DECLINE.name())) {
            meetingService.removeMeetingByChatId(chatIdLong);
            if (callbackData.contains(MEETING.name() + DECLINE.name())) {
                fillMeetingButtons(chatId, MEETING.name());
                return; // Если дополнительная логика не требуется, используем ранний выход
            }
            keyboard.mainMenu(chatId);
            return; // Ранний выход после обработки отказа
        }

        if (isMeeting || callbackData.contains(NEXT_WEEK.name())) {
            fillMeetingButtons(chatId, callbackData);
        } else if (callbackData.contains(DATE.name())) {
            meetingService.fillDayOfWeekAndMonth(meeting, user, callbackData);
            Set<String> occupiedSlots = meetingService.getOccupiedTimeSlots(meeting.getDayOfWeek(), meeting.getDayOfMonth());
            fillTimeButtons(chatId, occupiedSlots);
        } else if (callbackData.contains(TIME.name())) {
            meetingService.fillTime(user, callbackData);
            confirmationOfTheMeeting(user);
        } else if (callbackData.contains(APPROVE.name())) {
            meetingService.isActualMeeting(chatIdLong, true);
            keyboard.mainMenu(chatId);
        }
    }
    private void fillMeetingButtons (String chatId, String callbackData) {
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

    private void fillTimeButtons (String chatId, Set<String> occupiedSlots) {
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

    private void confirmationOfTheMeeting(User user) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(user.getChatId()));

        Meeting meeting = meetingService.getMeetingByChatId(user.getChatId());

        // Убедитесь, что meeting не null, прежде чем обращаться к его методам
        if (meeting != null) {
            String meetingDetails = meeting.getMeeting();
            message.setText("Подтвердите дату и время встречи: \n" + meetingDetails);
        } else {
            message.setText("Информация о встрече не найдена.");
        }

        message.setReplyMarkup(getKeyboardApproval());

        // Предполагается, что sender.sendMessage - это метод для отправки сообщения через бота
        sender.sendMessage(message);
    }

    private InlineKeyboardMarkup getKeyboardApproval() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> nextLine = new ArrayList<>();
        nextLine.add(createButtonMenu("Подтверждаю ✅", APPROVE.name()));


        rowsInline.add(nextLine);
        rowsInline.add(declineButtonInline());

        markup.setKeyboard(rowsInline);
        return markup;
    }
}
