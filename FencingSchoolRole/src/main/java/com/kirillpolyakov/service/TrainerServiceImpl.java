package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Trainer;
import com.kirillpolyakov.model.UserDetailsImpl;
import com.kirillpolyakov.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private TrainerRepository trainerRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Trainer add(Trainer trainer) {
        try {
            trainer.setPassword(this.passwordEncoder.encode(trainer.getPassword()));
            this.trainerRepository.save(trainer);
            return trainer;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин или электронная почта для тренера уже заняты");
        }
    }

    @Override
    public Trainer get(long trainerId) {
        return this.trainerRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Тренера не существует"));
    }

    @Override
    public Trainer getForApprentice(long id) {
        return this.trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Тренера не существует"));
    }

    @Override
    public List<Trainer> get() {
        return this.trainerRepository.findAll();
    }

    @Override
    public Trainer update(Trainer trainer) {
        Trainer old = this.get(trainer.getId());
        old.setUserName(trainer.getUserName());
        old.setSurname(trainer.getSurname());
        old.setName(trainer.getName());
        old.setPatronymic(trainer.getPatronymic());
        old.setExperience(trainer.getExperience());
        old.setEmail(trainer.getEmail());
        try {
            this.trainerRepository.save(old);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин или электронная почта для тренера уже заняты");
        }
        return old;
    }


    @Override
    public Trainer updateWithPassword(Trainer trainer) {
        Trainer old = this.get(trainer.getId());
        old.setUserName(trainer.getUserName());
        old.setSurname(trainer.getSurname());
        old.setName(trainer.getName());
        old.setPatronymic(trainer.getPatronymic());
        old.setExperience(trainer.getExperience());
        old.setEmail(trainer.getEmail());
        old.setPassword(passwordEncoder.encode(trainer.getPassword()));
        try {
            this.trainerRepository.save(old);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой логин или электронная почта для тренера уже заняты");
        }
        return old;
    }



    @Override
    public Trainer delete(long trainerId) {
        Trainer trainer = this.get(trainerId);
        this.trainerRepository.delete(trainer);
        return trainer;
    }


}
