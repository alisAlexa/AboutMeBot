package org.pack.telegram;

import org.pack.telegram.config.BotConfig;
import org.pack.telegram.entity.Meeting;
import org.pack.telegram.entity.User;
import org.pack.telegram.service.KeyboardService;
import org.pack.telegram.service.MeetingService;
import org.pack.telegram.service.UserService;
import org.pack.telegram.service.sender.MeetingSender;
import org.pack.telegram.service.sender.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static org.pack.telegram.enums.MeetingEnum.*;
import static org.pack.telegram.enums.MenuButtonsEnum.MEETING;
import static org.pack.telegram.enums.MenuButtonsEnum.*;
import static org.pack.telegram.util.StringExtractor.createMeetingId;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final BotConfig botConfig;
    private final MessageSender sender;
    private final MeetingSender meetingSender;
    private final KeyboardService keyboard;
    private final UserService userService;
    private final MeetingService meetingService;

    @Autowired
    public TelegramBot(BotConfig botConfig,
                       MessageSender sender,
                       MeetingSender meetingSender,
                       KeyboardService keyboard, UserService service,
                       MeetingService meetingService) {
        this.botConfig = botConfig;
        this.sender = sender;
        this.meetingSender = meetingSender;
        this.keyboard = keyboard;
        this.userService = service;
        this.meetingService = meetingService;
        log.info("TelegramBot worked motherfucker!!!");
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = String.valueOf(update.getMessage().getChatId());
            keyboard.mainMenu(chatId);
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

                meetingSender.handleMeetingInteraction(meeting, user, chatId, callbackData);

            } else if (callbackData.equals(String.valueOf(MENU))) {
                sender.deleteMessage(chatId, messageId);

                keyboard.mainMenu(chatId);
            }
        }
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
