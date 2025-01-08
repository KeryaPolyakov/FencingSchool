package com.kirillpolyakov.controller;

import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.TrainerSchedule;
import com.kirillpolyakov.service.TrainerScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Tag(name = "TrainerSchedule controller", description = "Контроллер для работы с распианием тренера")
@RestController
@RequestMapping("/trainer_schedule")
public class TrainerScheduleController {

    private TrainerScheduleService trainerScheduleService;

    @Autowired
    public void setTrainerScheduleService(TrainerScheduleService trainerScheduleService) {
        this.trainerScheduleService = trainerScheduleService;
    }

    @Operation(
            summary = "Добавление расписания тренера",
            description = "осуществляет добавление(так же будет работать и на обновление) расписания для конкретного" +
                    "тренера с заданным id, доступно самому Тренеру и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #trainerId or hasRole('ADMIN')")
    @PostMapping(path = "add/{trainerId}/params")
    public ResponseEntity<ResponseResult<TrainerSchedule>> add(@PathVariable @Parameter(description = "trainerId",
            example = "1", required = true) long trainerId, @RequestParam @Parameter(description = "name od day",
            example = "monday", required = true) String day, @RequestParam @Parameter(description = "time start",
            example = "08:00", required = true) LocalTime start, @RequestParam @Parameter(description = "time finish",
            example = "20:00", required = true)LocalTime finish) {
        try {
            this.trainerScheduleService.add(trainerId, day, start, finish);
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.get(trainerId)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Получение расписания тренера",
            description = "осуществляет получение расписания тренера с заданным id, доступно самому Тренеру, Администратору," +
                    "и ученикам"
    )
    @PreAuthorize("authentication.principal.id == #id or hasAnyRole('ADMIN, APPRENTICE')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> get(@PathVariable @Parameter(description = "id",
            example = "1", required = true) long id) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.get(id)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseResult<List<TrainerSchedule>>> get() {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.get()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * delete –осуществляет удаление расписания тренера с заданным id и
     * днем недели(подаем на английском языке в виде строки с маленькой буквы)(admin, trainer– только себя)
     */
    @Operation(
            summary = "Удаление расписания тренера",
            description = "Осуществляет удаление расписания тренера с заданным id, днем недели доступно самому Тренеру, " +
                    "Администратору"
    )
    @DeleteMapping(path = "delete/{trainerId}/day")
    public ResponseEntity<ResponseResult<TrainerSchedule>> get(@PathVariable @Parameter(description = "trainerId",
            example = "1", required = true) long trainerId, @Parameter(description = "name of day",
            example = "monday", required = true) String day) {
        try {
            return new ResponseEntity<>(
                    new ResponseResult<>(null, this.trainerScheduleService.delete(trainerId, day)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
