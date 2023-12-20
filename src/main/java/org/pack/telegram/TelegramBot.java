package org.pack.telegram;

import org.pack.telegram.config.BotConfig;
import org.pack.telegram.entity.Meeting;
import org.pack.telegram.entity.User;
import org.pack.telegram.service.MeetingService;
import org.pack.telegram.service.MessageSender;
import org.pack.telegram.service.UserService;
import org.pack.telegram.service.sender.MeetingSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.pack.telegram.enums.MeetingEnum.*;
import static org.pack.telegram.enums.MenuButtonsEnum.*;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.service.ButtonService.createButtonMenu;
import static org.pack.telegram.util.StringExtractor.createMeetingId;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final BotConfig botConfig;
    private final MessageSender sender;
    private final MeetingSender meetingSender;

    private final UserService userService;
    private final MeetingService meetingService;

    @Autowired
    public TelegramBot(BotConfig botConfig,
                       MessageSender sender,
                       MeetingSender meetingSender,
                       UserService service,
                       MeetingService meetingService) {
        this.botConfig = botConfig;
        this.sender = sender;
        this.meetingSender = meetingSender;
        this.userService = service;
        this.meetingService = meetingService;
        log.info("TelegramBot worked motherfucker!!!");
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

            User user = userService.checkUser(update.getCallbackQuery().getMessage().getChatId(),
                                update.getCallbackQuery().getMessage().getChat());

            if (callbackData.equals(String.valueOf(WORK_EXPERIENCE))) {
                sender.deleteMessage(chatId, messageId);

                sender.sendMessage(sender.fillTextMessage(chatId, "Мой опыт работы включает в себя..."));
            } else if (callbackData.equals(String.valueOf(TECHNICAL_SKILLS))) {
                sender.deleteMessage(chatId, messageId);

                sender.sendMessage(sender.fillTextMessage(chatId, "Мои технические навыки..."));
            } else if (isMeeting(callbackData)) {
                sender.deleteMessage(chatId, messageId);//"DATE_WEDNESDAY"
                Meeting meeting = new Meeting();
                meeting.setMeetingId(createMeetingId(chatId));
                //DATE_TUESDAY_5
                if (callbackData.contains(MEETING.name())) {
                    meetingSender.fillMeetingButtons(chatId, callbackData);
                } else if (callbackData.contains(DATE.name())) {
                    meetingService.fillDayOfWeekAndMonth(meeting, user, callbackData); // заполняем meeting днем недели и датой

                    Set<String> occupiedSlots =
                            meetingService.getOccupiedTimeSlots(meeting.getDayOfWeek(), meeting.getDayOfMonth());

                    meetingSender.fillTimeButtons(chatId, callbackData, occupiedSlots); // отправляем кнопки с выбором времени

                } else if (callbackData.contains(TIME.name())) {//there
                    meetingService.fillTime(user, callbackData); // заполняем meeting временем. после заполнения ничего не делаем!!!

                    meetingSender.confirmationOfTheMeeting(user);//подтверждение встречи
                } else if (callbackData.contains(APPROVE.name())) {
                    meetingService.isActualMeeting(Long.valueOf(chatId), true);
                    mainMenu(chatId);
                }

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
