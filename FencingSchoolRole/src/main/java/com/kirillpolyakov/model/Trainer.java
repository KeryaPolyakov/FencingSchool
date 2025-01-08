package com.kirillpolyakov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
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
@RequiredArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer extends User{

    @NonNull
    @Column
    private int experience;

    @NonNull
    @Column(unique = true)
    private String email;


    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<Training> training;

    @OneToOne(mappedBy = "trainer", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TrainerSchedule trainerSchedule;

    public Trainer(long id, @NonNull String userName, @NonNull String surname, @NonNull String name,
                   @NonNull String patronymic, int experience, String email) {
        super(id, userName, surname, name, patronymic);
        this.experience = experience;
        this.email = email;
    }

    public Trainer(@NonNull String userName, @NonNull String surname, @NonNull String name, @NonNull String patronymic, @NonNull int experience, @NonNull String email) {
        super(userName, surname, name, patronymic);
        this.experience = experience;
        this.email = email;
    }
}
