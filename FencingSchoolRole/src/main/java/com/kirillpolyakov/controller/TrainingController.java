package com.kirillpolyakov.controller;

import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.Training;
import com.kirillpolyakov.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("training")
public class TrainingController {

    private TrainingService trainingService;

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Operation(
            summary = "Добавление тренировки",
            description = "осуществляет добавление новой тренировки для заданного id ученика и для заданного id " +
                    "тренера, доступно самому Тренеру, самому ученику, Администратору"
    )
    @PreAuthorize("authentication.principal.id == #apprenticeId or authentication.principal.id == #trainerId " +
            "or hasRole('ADMIN')")
    @PostMapping("apprentice_trainer")
    public ResponseEntity<ResponseResult<Training>> add(@RequestBody Training training,
                                                        @RequestParam @Parameter(description = "apprenticeId",
                                                                example = "1", required = true) long apprenticeId,
                                                        @RequestParam @Parameter(description = "trainerId",
                                                                example = "1", required = true) long trainerId) {
        try {
            this.trainingService.add(training, apprenticeId, trainerId);
            return new ResponseEntity<>(new ResponseResult<>(null, training), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Получение тренировки",
            description = "Осуществляет получение тренировки по id, доступно самому Тренеру, самому ученику, Администратору"
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseResult<Training>> get(@PathVariable @Parameter(description = "Id",
            example = "1", required = true) long id) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.get(id)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Получение тренировок",
            description = "Осуществляет получение тренировок по id ученика, доступно Тренеру, самому ученику, Администратору"
    )
    @GetMapping(path = "apprentice/{apprenticeId}")
    public ResponseEntity<ResponseResult<List<Training>>> getByApprenticeId(@PathVariable @Parameter(description = "apprenticeId",
            example = "1", required = true) long apprenticeId) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.getByApprenticeId(apprenticeId)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Получение тренировок",
            description = "Осуществляет получение тренировок по id тренера, доступно самому Тренеру, Администратору"
    )
    @GetMapping(path = "trainer/{trainerId}")
    public ResponseEntity<ResponseResult<List<Training>>> getByTrainerId(@PathVariable @Parameter(description = "trainerId",
            example = "1", required = true) long trainerId) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.getByTrainerId(trainerId)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Получение свободного времени тренера",
            description = "Осуществляет получение тренера по id тренера, доступно самому Тренеру, Ученику, Администратору"
    )
    @GetMapping(path = "free_time/{trainerId}")
    public ResponseEntity<ResponseResult<List<String>>> getTime
            (@PathVariable @Parameter(description = "trainerId",
                    example = "1", required = true) long trainerId, @RequestParam @Parameter(description = "date",
                    example = "21.05.2021", required = true) LocalDate date,
             @RequestParam @Parameter(description = "number od gym",
                     example = "1", required = true) int numberGym) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null,
                    this.trainingService.getFreeTime(trainerId, date, numberGym)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Удаление тренировки",
            description = "Осуществляет удаление тренировки по Id, доступно самому Тренеру, самому Ученику, Администратору"
    )
    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<ResponseResult<Training>> delete(@PathVariable long id) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.delete(id)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
