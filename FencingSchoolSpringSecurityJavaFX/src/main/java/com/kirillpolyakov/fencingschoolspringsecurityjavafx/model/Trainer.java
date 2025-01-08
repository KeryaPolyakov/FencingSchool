package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import lombok.*;
import java.util.List;

/**
 * Trainer(наследуется отUser)– тренер школы фехтования, который проводит тренировки для учеников.
 * Каждый тренер имеет стаж, измеряемый в количестве лет, проработанных в данной сфере. Имеет следующие поля
 * experience
 * email
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Trainer extends User{

    private int experience;

    private String email;

    private TrainerSchedule trainerSchedule;

    private List<Training> training;

    public Trainer(long id, @NonNull String userName, @NonNull String surname, @NonNull String name,
                   @NonNull String patronymic, int experience, String email) {
        super(id, userName, surname, name, patronymic);
        this.experience = experience;
        this.email = email;
    }

    public Trainer(long id, @NonNull String userName, @NonNull String surname, @NonNull String name,
                   @NonNull String patronymic, @NonNull String password, int experience, String email) {
        super(id, userName, surname, name, patronymic, password);
        this.experience = experience;
        this.email = email;
    }

    public Trainer(@NonNull String userName, @NonNull String surname, @NonNull String name,
                   @NonNull String patronymic, @NonNull String password, int experience, String email) {
        super(userName, surname, name, patronymic, password);
        this.experience = experience;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Тренер: " + super.toString() +
                " стаж=" + experience +
                ", email='" + email;
    }
}
