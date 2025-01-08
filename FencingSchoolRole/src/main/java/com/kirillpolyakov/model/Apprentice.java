package com.kirillpolyakov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
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
@RequiredArgsConstructor
@Entity
@Table(name = "apprentices")
public class Apprentice extends User{

    @NonNull
    @Column(unique = true)
    private String phoneNumber;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apprentice")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<Training> trainings = new ArrayList<>();



}
