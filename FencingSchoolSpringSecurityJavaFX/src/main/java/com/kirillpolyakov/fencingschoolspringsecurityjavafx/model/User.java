package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.time.LocalDate;


/**
 * User– пользователь системы, имеет следующие поля
 * id
 * login
 * surname
 * name
 * patronymic
 * password
 * name
 * regDate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "admin"),
        @JsonSubTypes.Type(value = Trainer.class, name = "trainer"),
        @JsonSubTypes.Type(value = Apprentice.class, name = "apprentice"),
})
public class User {

    private long id;

    @NonNull
    private String userName;

    @NonNull
    private String surname;

    @NonNull
    private String name;

    @NonNull
    private String patronymic;

    @NonNull
    private String password;

    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate regData = LocalDate.now();

    public User(long id, @NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic
    , @NonNull String password) {
        this.id = id;
        this.userName = userName;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.password = password;
    }

    public User(long id, @NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic
            ) {
        this.id = id;
        this.userName = userName;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }

    @Override
    public String toString() {
        return surname + " " +
                name + " " +
                patronymic;
    }
}
