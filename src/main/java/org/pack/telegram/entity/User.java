package org.pack.telegram.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "users")
public class User {

    @Id
    @NotNull
    private Long chatId;

    private String userName;

    private String firstName;

    private Timestamp registeredAt;

    private String phoneNumber;

}
