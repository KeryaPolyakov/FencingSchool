package com.kirillpolyakov.service;

import com.kirillpolyakov.model.*;
import com.kirillpolyakov.repository.TrainingRepository;
import com.kirillpolyakov.util.Constants;
import com.kirillpolyakov.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;

    private ApprenticeService apprenticeService;

    private TrainerService trainerService;

    private TrainerScheduleService trainerScheduleService;

    @Autowired
    public void setApprenticeService(ApprenticeService apprenticeService) {
        this.apprenticeService = apprenticeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Autowired
    public void setTrainerSchedule(TrainerScheduleService trainerScheduleService) {
        this.trainerScheduleService = trainerScheduleService;
    }

    /**
     * post – осуществляет добавление новой тренировки для заданного id ученика и для заданного id тренера
     */

    @Override
    public void add(Training training, long apprenticeId, long trainerId) {
        String day = training.getDate().getDayOfWeek().toString().toLowerCase();
        Apprentice apprentice = this.apprenticeService.get(apprenticeId);
        Trainer trainer = this.trainerService.getForApprentice(trainerId);
        TrainerSchedule trainerSchedule = this.trainerScheduleService.getForApprentice(trainerId);
        LocalTime[] localTimes = trainerSchedule.get(day);
        LocalTime startTime = localTimes[0];
        if (startTime == null) {
            throw new IllegalArgumentException("Тренер не работает в это время");
        }
        LocalTime endTime = localTimes[1];
        if (training.getTimeStart().isBefore(startTime) || training.getTimeStart().plusMinutes(Constants.MINUTES)
                .isAfter(endTime)) {
            throw new IllegalArgumentException("Тренер не работает в это время");
        }
        if (this.count(this.getByTrainerIdAndDataAndNumberGym(trainer.getId(), training.getDate(),
                training.getNumberGym()), training.getTimeStart())>= 3) {
            throw new IllegalArgumentException("Слишком много тренеровок для одного тренера в одном зале");
        }
        List<Training> trainings = this.trainingRepository
                .findAllByDateAndNumberGym(training.getDate(), training.getNumberGym());
        if (this.count(trainings, training.getTimeStart()) >= 10) {
            throw new IllegalArgumentException("Слишком много тренировок для одного зала");
        }
        training.setApprentice(apprentice);
        training.setTrainer(trainer);
        try {
            this.trainingRepository.save(training);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("У ученика сегодня уже есть тренировка");
        }
    }

    private long count(List<Training> list, LocalTime trainingStart){
        return list.stream().filter(x -> Util.isOverlapping(x.getTimeStart(),
                x.getTimeStart().plusMinutes(Constants.MINUTES),
                trainingStart, trainingStart.plusMinutes(Constants.MINUTES))).count();
    }

    @Override
    public Training get(long trainingId) {
        Training training = this.trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Тренировки нет"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long id = userDetails.getId();
        List<GrantedAuthority> roles = (List<GrantedAuthority>) userDetails.getAuthorities();
        if (id != training.getApprentice().getId() && id != training.getTrainer().getId()
                && !roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("Тренировку может получить только администратор, " +
                    "ученик и тренер только свою тренировку");
        }
        return training;
    }

    @Override
    public List<Training> getByTrainerIdAndDataAndNumberGym(long trainerId, LocalDate date, int numberGym) {
        this.trainerService.getForApprentice(trainerId);
        return this.trainingRepository.findAllByTrainerIdAndDateAndNumberGym(trainerId, date, numberGym);
    }

    @Override
    public List<Training> getByApprenticeId(long apprenticeId) {
        this.apprenticeService.get(apprenticeId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long id = userDetails.getId();
        List<GrantedAuthority> roles = (List<GrantedAuthority>) userDetails.getAuthorities();
        if (id != apprenticeId && !roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && !roles.contains(new SimpleGrantedAuthority("ROLE_TRAINER"))) {
            throw new IllegalArgumentException("Тренировки может получить только администратор и тренер" +
                    "ученик только свои тренировки");
        }
        return this.trainingRepository.findAllByApprenticeId(apprenticeId);
    }

    @Override
    public List<Training> getByTrainerId(long trainerId) {
        this.trainerService.getForApprentice(trainerId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long id = userDetails.getId();
        List<GrantedAuthority> roles = (List<GrantedAuthority>) userDetails.getAuthorities();
        if (id != trainerId && !roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("Тренировки может получить только администратор, " +
                    "тренер только свои тренировки");
        }
        return this.trainingRepository.findAllByTrainerId(trainerId);
    }

    @Override
    public Training getForDelete(long id) {
        return this.trainingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Такой тренировки нет"));
    }

    @Override
    public Training delete(long trainingId) {
        Training training = this.getForDelete(trainingId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long id = userDetails.getId();
        List<GrantedAuthority> roles = (List<GrantedAuthority>) userDetails.getAuthorities();
        if (id != training.getApprentice().getId() && id != training.getTrainer().getId()
                && !roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("Тренировку может удалить только администратор, " +
                    "ученик и тренер только свою тренировку");
        }
        this.trainingRepository.delete(training);
        return training;
    }

    @Override
    public List<String> getFreeTime(long trainerId, LocalDate date, int numberGym) {
        String day = date.getDayOfWeek().toString().toLowerCase();
        TrainerSchedule trainerSchedule = this.trainerScheduleService.get(trainerId);
        LocalTime[] localTimes = trainerSchedule.get(day);
        LocalTime startTime = localTimes[0];
        LocalTime endTime = localTimes[1];
        List<Training> trainings = this.getByTrainerIdAndDataAndNumberGym(trainerId, date, numberGym);
        List<Training> allTrainings = this.trainingRepository.findAllByDateAndNumberGym(date, numberGym);
        List<String> time = Constants.TIME_START;
        time = time.stream().filter(x -> this.count(trainings, LocalTime.parse(x)) < 3
                && this.count(allTrainings, LocalTime.parse(x)) < 10
                && LocalTime.parse(x).isAfter(startTime.minusMinutes(1))
                && LocalTime.parse(x).isBefore(endTime.minusMinutes(Constants.MINUTES - 1))).toList();
        if (date.equals(LocalDate.now())) {
            time = time.stream().filter(x -> LocalTime.parse(x).isAfter(LocalTime.now())).toList();
        }
        return time;
    }
}
