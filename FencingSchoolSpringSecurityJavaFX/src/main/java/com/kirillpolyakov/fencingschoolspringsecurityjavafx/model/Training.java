package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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
public class Training {

    private long id;

    @NonNull
    private int numberGym;

    @NonNull
    private Trainer trainer;

    @NonNull
    private Apprentice apprentice;

    @NonNull
    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @NonNull
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime timeStart;
}
