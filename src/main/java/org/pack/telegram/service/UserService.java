package org.pack.telegram.service;

import org.apache.commons.lang3.StringUtils;
import org.pack.telegram.TelegramBot;
import org.pack.telegram.entity.User;
import org.pack.telegram.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Component
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    /**
     * Проверяем существует ли User, если да, находим и возвращаем
     * Если не находим создаем и возвращаем
     * @param chatId
     * @param chat
     * @return User
     */
    public User checkUser(Long chatId, Chat chat) {

        if (isPresent(chatId))
            return findUser(chatId);

        return createUser(chatId, chat);
    }

    private boolean isPresent(Long chatId) {
        return repository.findById(chatId).isPresent();
    }

    private User findUser(Long chatId) {
        log.info("User with chatId: {}, founded", chatId);
        return repository.findById(chatId).get();
    }

    private User createUser(Long chatId, Chat chat) {
        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setUserName(chat.getUserName());

        repository.save(user);
        log.info("User with chatId: {}, created", chatId);

        return user;
    }
}
