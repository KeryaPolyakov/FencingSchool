package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Trainer;
import com.kirillpolyakov.model.TrainerSchedule;
import com.kirillpolyakov.model.UserDetailsImpl;
import com.kirillpolyakov.repository.TrainerScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class TrainerScheduleServiceImpl implements TrainerScheduleService {

    private TrainerScheduleRepository trainerScheduleRepository;

    private TrainerService trainerService;

    @Autowired
    public void setTrainerScheduleRepository(TrainerScheduleRepository trainerScheduleRepository) {
        this.trainerScheduleRepository = trainerScheduleRepository;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    /**
     * post – осуществляет добавление(так же будет работать и на обновление) расписания
     * для конкретного тренера с заданным id, днем недели(подаем на английском языке в
     * виде строки с маленькой буквы), времени начала и конца работы в этот день
     */

    @Override
    public void add(long trainerId, String day, LocalTime start, LocalTime finish) {
        Trainer trainer = this.trainerService.get(trainerId);
        TrainerSchedule trainerSchedule = this.trainerScheduleRepository.findById(trainerId).orElse(new TrainerSchedule(trainer));
        trainerSchedule.set(day, start, finish);
        this.trainerScheduleRepository.save(trainerSchedule);
    }

    /**
     * осуществляет получение расписания для тренера с заданным id
     */

    @Override
    public TrainerSchedule get(long trainerScheduleId) {
        return this.trainerScheduleRepository.findById(trainerScheduleId).orElseThrow(() -> new IllegalArgumentException("Расписания не существует"));
    }

    @Override
    public TrainerSchedule getForApprentice(long trainerId) {
        return this.trainerScheduleRepository.findById(trainerId).orElseThrow(() -> new IllegalArgumentException("Расписания не существует"));
    }

    @Override
    public List<TrainerSchedule> get() {
        return this.trainerScheduleRepository.findAll();
    }

    /**
     * осуществляет удаление расписания тренера с заданным id и
     * днем недели(подаем на английском языке в виде строки с заглавной буквы)
     */
    @PreAuthorize("authentication.principal.id == #trainerId or hasRole('ADMIN')")
    @Override
    public TrainerSchedule delete(long trainerId, String day) {
        TrainerSchedule trainerSchedule = this.get(trainerId);
        trainerSchedule.delete(day);
        this.trainerScheduleRepository.save(trainerSchedule);
        return trainerSchedule;
    }
}
