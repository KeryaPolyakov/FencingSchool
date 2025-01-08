package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Apprentice(наследуется отUser) –ученик школы фехтования, который посещает
 * тренировки у разных тренеров.Имеет следующие поля
 * phoneNumber
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
public class Apprentice extends User{

    private String phoneNumber;

    private List<Training> trainings = new ArrayList<>();

    public Apprentice(@NonNull String userName, @NonNull String surname, @NonNull String name,
                      @NonNull String patronymic, @NonNull String password, String phoneNumber) {
        super(userName, surname, name, patronymic, password);
        this.phoneNumber = phoneNumber;
    }

    public Apprentice(long id, @NonNull String userName, @NonNull String surname, @NonNull String name,
                      @NonNull String patronymic, String phoneNumber) {
        super(id, userName, surname, name, patronymic);
        this.phoneNumber = phoneNumber;
    }

    public Apprentice(long id, @NonNull String userName, @NonNull String surname, @NonNull String name,
                      @NonNull String patronymic, @NonNull String password, String phoneNumber) {
        super(id, userName, surname, name, patronymic, password);
        this.phoneNumber = phoneNumber;
    }


}
