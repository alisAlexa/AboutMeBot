package org.pack.telegram.service;

import org.pack.telegram.entity.Meeting;
import org.pack.telegram.entity.User;
import org.pack.telegram.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.pack.telegram.util.StringExtractor.extractDayOfMonth;
import static org.pack.telegram.util.StringExtractor.extractDayOfWeek;

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
        meeting.setActual(false);//пока лишь заполняем так что не потверждено
    }

    public void fillTime(Meeting meeting, User user, String callbackData) {
    }

    public Set<String> getOccupiedTimeSlots(String dayOfWeek, int dayOfMonth) {
        List<Meeting> meetings = repository.findByDayOfWeekAndDayOfMonth(dayOfWeek, dayOfMonth);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return meetings.stream()
                .map(Meeting::getTime)
                .map(time -> time.format(formatter))
                .collect(Collectors.toSet());
    }
}
