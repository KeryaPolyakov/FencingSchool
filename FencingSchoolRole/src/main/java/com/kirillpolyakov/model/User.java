package com.kirillpolyakov.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import javax.persistence.*;
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
@Entity
@Table(name = "users")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "admin"),
        @JsonSubTypes.Type(value = Trainer.class, name = "trainer"),
        @JsonSubTypes.Type(value = Apprentice.class, name = "apprentice"),
})
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String userName;

    @NonNull
    @Column(nullable = false)
    private String surname;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String patronymic;

    @Column(nullable = false)
    private String password;

    @Column
    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate regData = LocalDate.now();

    public User(long id, @NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic
    ) {
        this.id = id;
        this.userName = userName;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
    }
}
