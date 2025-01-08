package com.kirillpolyakov.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.Admin;
import com.kirillpolyakov.model.Trainer;
import com.kirillpolyakov.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Tag(name = "Trainer controller", description = "Контроллер для работы с тренерами")
@RestController
@RequestMapping("trainer")
public class TrainerController {

    private TrainerService trainerService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        {
            objectMapper.registerModule(new JavaTimeModule());
        }
    }

    @Operation(
            summary = "Регистрация тренера",
            description = "Позволяет зарегистрировать нового тренера, доступно Администратору"
    )
    @PostMapping("/add")
    public void add(HttpServletResponse response, @RequestBody Trainer trainer) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Trainer>() {
                }).writeValue(response.getWriter(),
                        this.trainerService.add(trainer));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Получение всех тренеров",
            description = "Позволяет получить всех тренеров, доступно Администратору и Ученику"
    )
    @GetMapping("/getAll")
    public void get(HttpServletResponse response) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<List<Trainer>>() {
                }).writeValue(response.getWriter(),
                        this.trainerService.get());
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Получение тренера",
            description = "Позволяет получить тренера по Id, доступно Администратору и самому Тренеру"
    )
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    @GetMapping("/get/{id}")
    public void get(HttpServletResponse response, @PathVariable @Parameter(description = "id",
            example = "1", required = true) long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Trainer>() {
                }).writeValue(response.getWriter(),
                        this.trainerService.get(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Обновление тренера без пароля",
            description = "Позволяет обновить данные тренера без пароля, доступно самому Тренеру и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #trainer.id or hasRole('ADMIN')")
    @PutMapping("/update")
    public void update(HttpServletResponse response, @RequestBody Trainer trainer) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Trainer>() {
                }).writeValue(response.getWriter(),
                        this.trainerService.update(trainer));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(),
                        new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Обновление тренера с пароля",
            description = "Позволяет обновить данные тренера с паролем, доступно самому Тренеру и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #trainer.id or hasRole('ADMIN')")
    @PutMapping("/update/password")
    public void updateWithPassword(HttpServletResponse response, @RequestBody Trainer trainer) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Trainer>() {
                }).writeValue(response.getWriter(),
                        this.trainerService.updateWithPassword(trainer));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(),
                        new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Удаление тренера",
            description = "Позволяет удалить тренера по Id, доступно самому Тренеру и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void delete(HttpServletResponse response, @PathVariable @Parameter(description = "id",
            example = "1", required = true)long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Trainer>() {
                }).writeValue(response.getWriter(),
                        this.trainerService.delete(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
