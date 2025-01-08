package com.kirillpolyakov.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Admin(наследуется отUser) – администратор школы фехтования. Имеет следующие поля
 * email
 * salary
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "admins")
@ToString(callSuper = true)
public class Admin extends User {


    @NonNull
    @Column(unique = true)
    private String email;

    @NonNull
    @Column
    private int salary;

    public Admin(long id, @NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic, String email, int salary) {
        super(id, userName, surname, name, patronymic);
        this.email = email;
        this.salary = salary;
    }
}
