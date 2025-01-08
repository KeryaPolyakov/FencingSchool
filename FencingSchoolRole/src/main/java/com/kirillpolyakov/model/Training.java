package com.kirillpolyakov.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Training – тренировка, проводимая в школе фехтования. Имеет номер зала,
 * дату и время проведения занятия, а так же тренера и ученика. При добавлении новой тренировки
 * для тренера, необходимо учитывать, что тренер одновременно не может принимать более 3 учеников,
 * а так же не ведет прием в нерабочее время. Одновременно в зале могут заниматься только 10 учеников.
 * Ученик в один день не может присутствовать сразу на нескольких тренировках.
 * id
 * numberGym
 * trainer
 * apprentice
 * date
 * timeStart
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "trainings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"apprentice_id", "date"})})
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NonNull
    private int numberGym;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "apprentice_id", nullable = false)
    private Apprentice apprentice;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime timeStart;
}
