package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Admin(наследуется от User) – администратор школы фехтования. Имеет следующие поля
 * email
 * salary
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Admin extends User {

    private String email;

    private int salary;

    public Admin(long id, @NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic, String email, int salary) {
        super(id, userName, surname, name, patronymic);
        this.email = email;
        this.salary = salary;
    }

    public Admin(@NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic, @NonNull String password, String email, int salary) {
        super(userName, surname, name, patronymic, password);
        this.email = email;
        this.salary = salary;
    }

    public Admin(long id, @NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic, @NonNull String password, String email, int salary) {
        super(id, userName, surname, name, patronymic, password);
        this.email = email;
        this.salary = salary;
    }
}
