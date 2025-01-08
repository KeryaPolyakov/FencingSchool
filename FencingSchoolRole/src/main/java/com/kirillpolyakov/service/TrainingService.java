package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    void add (Training training, long apprenticeId, long trainerId);

    Training get(long id);

    List<Training> getByTrainerIdAndDataAndNumberGym(long trainerId, LocalDate date, int numberGym);

    List<Training> getByApprenticeId(long apprenticeId);

    List<Training> getByTrainerId(long trainerId);

    Training getForDelete(long id);

    Training delete(long id);

    List<String> getFreeTime(long trainerId, LocalDate date, int numberGym);
}
