package org.pack.telegram.repository;

import org.pack.telegram.entity.Meeting;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends CrudRepository<Meeting, Long> {

    List<Meeting> findByDayOfWeekAndDayOfMonth(String dayOfWeek, int dayOfMonth);

    List<Meeting> findByChatId(Long chatId);
}
