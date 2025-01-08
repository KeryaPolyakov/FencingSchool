package com.kirillpolyakov.repository;

import com.kirillpolyakov.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findAllByDateAndNumberGym(LocalDate date, int gym);

    List<Training> findAllByTrainerIdAndDateAndNumberGym(long TrainerId, LocalDate date, int numberGym);

    List<Training> findAllByApprenticeId(long id);

    List<Training> findAllByTrainerId(long id);


}
