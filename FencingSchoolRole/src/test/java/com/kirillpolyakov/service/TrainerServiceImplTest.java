package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Trainer;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.model.UserDetailsImpl;
import com.kirillpolyakov.repository.TrainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void update() {
        Trainer newTrainerInfo = new Trainer(6, "b", "b", "b", "b", 5, "b");
        //TODO нужна ли следующая строка?

        Trainer oldTrainerInfo = new Trainer();
        when(trainerRepository.save(any())).thenReturn(oldTrainerInfo);
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(oldTrainerInfo));

        trainerService.update(newTrainerInfo);

        assertEquals(newTrainerInfo.getUserName(), oldTrainerInfo.getUserName());
        verify(trainerRepository, times(1)).findById(anyLong());
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void updateExist(){
        Trainer newTrainerInfo = new Trainer(6, "b", "b", "b", "b", 5, "b");
        Trainer oldTrainerInfo = new Trainer();
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(oldTrainerInfo));
        doThrow(new IllegalArgumentException("Такой логин или электронная почта для тренера уже заняты"))
                .when(trainerRepository).save(any());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> trainerService.update(newTrainerInfo));

        assertEquals("Такой логин или электронная почта для тренера уже заняты", thrown.getMessage());
    }
    @Test
    void updateDoesntExist() {
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> trainerService.update(new Trainer()));

        assertEquals("Тренера не существует", thrown.getMessage());
    }

    @Test
    void add(){
        Trainer newTrainerInfo = new Trainer(1,"b", "b", "b", "b", 5, "b");
        when(trainerRepository.save(any())).thenReturn(newTrainerInfo);

        trainerService.add(newTrainerInfo);

        verify(trainerRepository, times(1)).save(newTrainerInfo);
    }
    @Test
    void addFail(){
        doThrow(new IllegalArgumentException("Такой логин или электронная почта для тренера уже заняты"))
                .when(trainerRepository).save(any());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> trainerService.add(new Trainer()));

        assertEquals("Такой логин или электронная почта для тренера уже заняты", thrown.getMessage());
    }

    @Test
    void getDoesntExist(){
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> trainerService.get(anyLong()));

        assertEquals("Тренера не существует", thrown.getMessage());
    }
    @Test
    void getDoesntExist2(){
        doThrow(new IllegalArgumentException("Тренера не существует"))
                .when(trainerRepository).findById(anyLong());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> trainerService.get(anyLong()));

        assertEquals("Тренера не существует", thrown.getMessage());
    }

    @Test
    void get() {
        Trainer newTrainerInfo = new Trainer(1,"b", "b", "b", "b", 5, "b");
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(newTrainerInfo));

        assertEquals(trainerService.get(anyLong()), newTrainerInfo);
    }

    @Test
    void getAll() {
        Trainer trainer1 = new Trainer(1,"b", "b", "b", "b", 5, "b");
        Trainer trainer2 = new Trainer(2,"a", "a", "a", "a", 5, "a");
        List<Trainer> trainers = List.of(trainer1, trainer2);
        when(trainerRepository.findAll()).thenReturn(trainers);

        assertIterableEquals(trainerService.get(), trainers);

    }

    @Test
    void delete() {
        Trainer trainer = new Trainer(1,"b", "b", "b", "b", 5, "b");
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(trainer));

        trainerService.delete(trainer.getId());

        verify(trainerRepository, times(1)).delete(trainer);
        assertEquals(trainerService.delete(trainer.getId()), trainer);
    }

    @Test
    void deleteDoesntExist(){
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> trainerService.delete(anyLong()));

        assertEquals("Тренера не существует", thrown.getMessage());
    }




}