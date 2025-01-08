package com.kirillpolyakov.service;

import com.kirillpolyakov.model.TrainerSchedule;

import java.time.LocalTime;
import java.util.List;

public interface TrainerScheduleService {

    void add(long id, String day, LocalTime start, LocalTime finish);

    TrainerSchedule get(long trainerId);

    TrainerSchedule getForApprentice(long trainerId);

    TrainerSchedule delete(long trainerId, String day);

    List<TrainerSchedule> get();


}
