package org.pack.telegram.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity(name = "meetings")
public class Meeting {

    @Id
    @NotNull
    private Long meetingId;

    private Long chatId;

    private String userName;

    private Date dateAndTime;

    private String link;

    private String note;
}
