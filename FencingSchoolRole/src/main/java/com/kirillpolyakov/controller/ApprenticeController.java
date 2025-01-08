package com.kirillpolyakov.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.Admin;
import com.kirillpolyakov.model.Apprentice;
import com.kirillpolyakov.service.ApprenticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Tag(name = "Apprentice controller", description = "Контроллер для работы с учениками")
@RestController
@RequestMapping("apprentice")
public class ApprenticeController {

    private ApprenticeService apprenticeService;

    private ObjectMapper objectMapper;

    @Autowired
    public void setApprenticeService(ApprenticeService apprenticeService) {
        this.apprenticeService = apprenticeService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        {
            objectMapper.registerModule(new JavaTimeModule());
        }
    }

    @Operation(
            summary = "Регистрация ученика",
            description = "Позволяет зарегистрировать нового ученика, доступно всем пользователям"
    )
    @PostMapping
    public void add(HttpServletResponse response, @RequestBody Apprentice apprentice) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Apprentice>() {
                }).writeValue(response.getWriter(),
                        this.apprenticeService.add(apprentice));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Получение всех учеников",
            description = "Позволяет получить всех учеников, доступно Администратору и Тренеру"
    )
    @GetMapping("/getAll")
    public void get(HttpServletResponse response) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<List<Apprentice>>() {
                }).writeValue(response.getWriter(),
                        this.apprenticeService.get());
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Получение ученика",
            description = "Позволяет получить ученика по Id, доступно Администратору и Тренеру"
    )
    @GetMapping("get/{id}")
    public void get(HttpServletResponse response, @PathVariable long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Apprentice>() {
                }).writeValue(response.getWriter(),
                        this.apprenticeService.get(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Обновление ученика без пароля",
            description = "Позволяет обновить данные ученика без пароля, доступно самому Ученику и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #apprentice.id or hasRole('ADMIN')")
    @PutMapping("/update")
    public void update(HttpServletResponse response, @RequestBody Apprentice apprentice) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Apprentice>() {
                }).writeValue(response.getWriter(),
                        this.apprenticeService.update(apprentice));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Обновление ученика с пароля",
            description = "Позволяет обновить данные ученика с пароля, доступно самому Ученику и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #apprentice.id or hasRole('ADMIN')")
    @PutMapping("/update/password")
    public void updateWithPassword(HttpServletResponse response, @RequestBody Apprentice apprentice) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Apprentice>() {
                }).writeValue(response.getWriter(),
                        this.apprenticeService.updateWithPassword(apprentice));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Удаление ученика",
            description = "Позволяет удалить ученика по Id, доступно самому Ученику и Администратору"
    )
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void delete(HttpServletResponse response, @PathVariable long id) {
        try {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(200);
                objectMapper.writerFor(new TypeReference<Apprentice>() {
                }).writeValue(response.getWriter(),
                        this.apprenticeService.delete(id));
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), new ResponseResult<>(e.getMessage(), null));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
