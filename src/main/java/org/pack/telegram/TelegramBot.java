package org.pack.telegram;

import org.pack.telegram.config.BotConfig;
import org.pack.telegram.service.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pack.telegram.MenuButton.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    // Словарь для хранения последнего идентификатора сообщения для каждого чата
    private final Map<String, Integer> lastMessageIds = new HashMap<>();
    private final BotConfig botConfig;
    private final MessageSender sender;

    @Autowired
    public TelegramBot(BotConfig botConfig, MessageSender sender) {
        this.botConfig = botConfig;
        this.sender = sender;
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
            int messageId = update.getCallbackQuery().getMessage().getMessageId();  // Получение ID сообщения


            if (callbackData.equals(String.valueOf(WORK_EXPERIENCE))) {

                sender.deleteMessage(chatId, messageId);//Удаляем последнее сообщение

                SendMessage responseMessage = new SendMessage();
                responseMessage.setChatId(String.valueOf(chatId));
                responseMessage.setText("Мой опыт работы включает в себя...");
                responseMessage.setReplyMarkup(backButton());

                sender.sendMessage(responseMessage);
            } else if (callbackData.equals(String.valueOf(TECHNICAL_SKILLS))) {
                sender.deleteMessage(chatId, messageId);//Удаляем последнее сообщение

                SendMessage responseMessage = new SendMessage();
                responseMessage.setChatId(String.valueOf(chatId));
                responseMessage.setText("Мои технические навыки...");
                responseMessage.setReplyMarkup(backButton());

                sender.sendMessage(responseMessage);
            } else if (callbackData.equals(String.valueOf(MENU_BUTTON))) {
                sender.deleteMessage(chatId, messageId);//Удаляем последнее сообщение

                mainMenu(chatId);
            }
        }
    }

    /**
     * Метод создает кнопку у сообщения, для возврата в меню
     * @return InlineKeyboardMarkup
     */
    private InlineKeyboardMarkup backButton() {
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Вернуться в главное меню");
        backButton.setCallbackData(String.valueOf(MENU_BUTTON));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(backButton);
        rows.add(row);
        keyboardMarkup.setKeyboard(rows);

        return keyboardMarkup;
    }

    /**
     * Метод отвечает за главное меню
     * @param chatId
     */
    private void mainMenu(String chatId) {
        InlineKeyboardMarkup markup = getInlineKeyboardMarkup();

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
    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {//меню в сообщении
        InlineKeyboardMarkup markup  = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        rowInline.add(createButtonMenu("Мой опыт работы",
                String.valueOf(WORK_EXPERIENCE)));

        rowInline.add(createButtonMenu("Мои технические навыки",
                String.valueOf(TECHNICAL_SKILLS)));

        rowsInline.add(rowInline);

        markup.setKeyboard(rowsInline);
        return markup;
    }

    /**
     * Метод отвечает за создание кнопок для меню
     * @param text - текст который будет выведен на кнопке
     * @param callbackData - для идентификации ботом нужных команд
     * @return button
     */
    private static InlineKeyboardButton createButtonMenu(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    @Override
    public String getBotUsername() {
        log.info("Telegram bot with name: {}, worked!", botConfig.getNameBot());

        return botConfig.getNameBot();
    }

    @Override
    public String getBotToken() {
        return botConfig.getTokenBot();
    }

    public void sendReplyKeyboard(String chatId) {//Интерактивное меню
/*
        String receivedText = update.getMessage().getText();
        switch (Objects.requireNonNull(receivedText)) {
            case "МЕНЮ":
                // Пользователь нажал на "Option 1"
                // ... выполните действие для "Option 1" ...
                for (Map.Entry<String, Integer> entry : lastMessageIds.entrySet()) {
                    deleteMessage(entry.getKey(), entry.getValue());
                }
                sendTextMessage(chatId, "Вы выбрали A!");
                break;
            case "Задать вопрос":
                sendTextMessage(chatId, "Вы выбрали B!");

                InlineKeyboardMarkup markup = getInlineKeyboardMarkup();

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Here are your options: ");
                message.setReplyMarkup(markup);

                sendMessage(message);
                break;
        }*/

        sendReplyKeyboard(chatId);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        //Создание рядов кнопок
        KeyboardRow rowA = new KeyboardRow();
        rowA.add("МЕНЮ");
        keyboard.add(rowA);

        KeyboardRow rowB = new KeyboardRow();
        rowB.add("ЗАДАТЬ ВОПРОС");
        rowB.add("ПОМОЩЬ");
        keyboard.add(rowB);

        keyboardMarkup.setKeyboard(keyboard);

        SendMessage message  = new SendMessage();
        message.setChatId(chatId);
        message.setReplyMarkup(keyboardMarkup);

        sender.sendMessageAndSaveIDMessage(lastMessageIds, message);
    }

}
