package org.pack.telegram.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@Setter
@Entity(name = "meetings")
public class Meeting {

    @Id
    @NotNull
    private Long meetingId;

    private Long chatId;

    private String dayOfWeek;

    private int dayOfMonth;

    private String month;

    private LocalTime time;

    private String link;

    private String note;

    private boolean isActual;

    public String getMeeting() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = time.format(timeFormatter);

        return " ⏰ " + formattedTime + " 📅 " + dayOfMonth + " " + month + " (" + dayOfWeek + ")";
    }
}
