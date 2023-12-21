package org.pack.telegram.service;

import org.pack.telegram.entity.Meeting;
import org.pack.telegram.entity.User;
import org.pack.telegram.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.pack.telegram.service.TimeService.extractTime;
import static org.pack.telegram.util.StringExtractor.*;

/**
 * Класс содержит логику связанную с сущностью Meeting
 */
@Component
public class MeetingService {

    private final MeetingRepository repository;

    @Autowired
    public MeetingService (MeetingRepository repository) {
        this.repository = repository;
    }



    public void fillDayOfWeekAndMonth(Meeting meeting, User user, String callbackData) {
        meeting.setChatId(user.getChatId());
        meeting.setDayOfWeek(extractDayOfWeek(callbackData));
        meeting.setDayOfMonth(extractDayOfMonth(callbackData));
        meeting.setMonth(extractMonth(callbackData));
        meeting.setActual(false);//пока лишь заполняем так что не потверждено
        repository.save(meeting);
    }

    public void fillTime(User user, String callbackData) {
        updateTime(user.getChatId(),
                extractTime(callbackData));
    }

    /**
     * Метод возвращает занятые слоты времени
     * @param dayOfWeek
     * @param dayOfMonth
     * @return
     */
    public Set<String> getOccupiedTimeSlots(String dayOfWeek, int dayOfMonth) {
        List<Meeting> meetings = repository.findByDayOfWeekAndDayOfMonth(dayOfWeek, dayOfMonth);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return meetings.stream()
                .map(Meeting::getTime)
                .filter(Objects::nonNull) // Фильтрация для исключения null значений
                .map(time -> time.format(formatter))
                .collect(Collectors.toSet());
    }

    /**
     * Метод заполняет время в записи(в сущности Meeting)
     * @param chatId
     * @param newTime
     */
    public void updateTime(Long chatId, LocalTime newTime) {
        Meeting meeting = getMeetingByChatId(chatId);

        meeting.setTime(newTime);
        repository.save(meeting);
    }

    public void isActualMeeting(Long chatId, boolean isActual) {
        Meeting meeting = getMeetingByChatId(chatId);

        meeting.setActual(isActual);
        repository.save(meeting);
    }

    public void removeMeetingByChatId(Long chatId) {
        Meeting meeting = getMeetingByChatId(chatId);

        repository.delete(meeting);
    }

    /**
     * Получить встречу(Meeting) по id
     * @param chatId
     * @return
     */
    public Meeting getMeetingByChatId(Long chatId) {
        List<Meeting> meetings = repository.findByChatId(chatId);
        if (meetings.isEmpty()) {
            throw new IllegalArgumentException("Meeting with chatId " + chatId + " not found!");
        }

        // Возвращаем самую последнюю встречу
        return meetings.get(meetings.size() - 1);
    }
}
