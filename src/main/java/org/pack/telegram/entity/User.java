package org.pack.telegram.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    private Long chatId;

    private String firstName;

    private String userName;

    private Timestamp registeredAt;
}
